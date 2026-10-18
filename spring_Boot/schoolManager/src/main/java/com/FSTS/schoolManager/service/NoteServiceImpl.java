package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.dtos.*;
import com.FSTS.schoolManager.entities.*;
import com.FSTS.schoolManager.entities.Module;
import com.FSTS.schoolManager.exception.*;
import com.FSTS.schoolManager.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    // Constants for grade calculations
    private static final double EXAM_WEIGHT = 0.75;
    private static final double TP_WEIGHT = 0.25;
    private static final double PASSING_GRADE = 10.0;
    private static final double MIN_GRADE = 0.0;
    private static final double MAX_GRADE = 20.0;
    private static final double SUSPICIOUS_GRADE_THRESHOLD = 5.0;
    private static final double STANDARD_DEVIATION_MULTIPLIER = 2.0;

    private final ModuleRepository moduleRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final NoteRepository noteRepository;
    private final NoteFinalRepository noteFinalRepository;
    private final EnseignantRepository enseignantRepository;
    private final FiliereRepository filiereRepository;
    private final EtudiantRepository etudiantRepository;
    private final ModuleAffichRepository moduleAffichRepository;
    private final SemesterNoteRepository semesterNoteRepository;
    private final JavaMailSender mailSender;
    private final FiliereSemesterRepository filiereSemesterRepository;
    private final AttendanceService attendanceService;

    @Override
    public void insertNotes(List<EtudiantNoteDto> noteList, Long idModule, Integer idEnseignant, 
            Integer idFiliere, Note.TypeExamen typeExamen) {
        
        log.info("Inserting {} notes for module {}, teacher {}, filiere {}, exam type {}", 
            noteList != null ? noteList.size() : 0, idModule, idEnseignant, idFiliere, typeExamen);
        
        validateInsertNotesParameters(noteList, idModule, idEnseignant, idFiliere, typeExamen);

        // Fetch required entities in batch
        EntityContainer entities = fetchRequiredEntities(idModule, idEnseignant, idFiliere);
        
        // Process notes based on exam type
        NoteProcessingResult result = processNotesByExamType(noteList, entities, typeExamen);

        // Batch save all entities
        saveNotesInBatch(result);
        
        // Send email notifications
        sendNoteNotifications(result.getNotesToSave(), entities.getModule());
        
        log.info("Successfully inserted {} notes and {} final grades", 
            result.getNotesToSave().size(), result.getFinalGradesToSave().size());
    }

    private void validateInsertNotesParameters(List<EtudiantNoteDto> noteList, Long idModule, 
            Integer idEnseignant, Integer idFiliere, Note.TypeExamen typeExamen) {
        
        if (CollectionUtils.isEmpty(noteList)) {
            throw new IllegalArgumentException("Note list cannot be null or empty");
        }
        
        if (Objects.isNull(idModule) || Objects.isNull(idEnseignant) || 
            Objects.isNull(idFiliere) || Objects.isNull(typeExamen)) {
            throw new IllegalArgumentException("Required parameters cannot be null");
        }

        // Validate individual notes
        noteList.forEach(this::validateNoteDto);
    }

    private void validateNoteDto(EtudiantNoteDto noteDto) {
        if (noteDto == null || noteDto.getCodeApj() == null) {
            throw new IllegalArgumentException("Invalid note DTO: missing student code");
        }
        
        if (noteDto.getNoteExam() < MIN_GRADE || noteDto.getNoteExam() > MAX_GRADE) {
            throw new IllegalArgumentException(
                String.format("Exam grade must be between %.1f and %.1f", MIN_GRADE, MAX_GRADE));
        }
        
        if (noteDto.getNoteTp() != null && 
            (noteDto.getNoteTp() < MIN_GRADE || noteDto.getNoteTp() > MAX_GRADE)) {
            throw new IllegalArgumentException(
                String.format("TP grade must be between %.1f and %.1f", MIN_GRADE, MAX_GRADE));
        }
    }

    private EntityContainer fetchRequiredEntities(Long idModule, Integer idEnseignant, Integer idFiliere) {
        Module module = moduleRepository.findById(idModule)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + idModule));
        
        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));
        
        Filiere filiere = filiereRepository.findById(idFiliere)
            .orElseThrow(() -> new FiliereNotFoundException("Filiere not found with id=" + idFiliere));
        
        Enseignant enseignant = enseignantRepository.findById(idEnseignant)
            .orElseThrow(() -> new EnseignantNotFoundException("Teacher not found with id=" + idEnseignant));

        return new EntityContainer(module, anneeUniversitaire, filiere, enseignant);
    }

    private NoteProcessingResult processNotesByExamType(List<EtudiantNoteDto> noteList, 
            EntityContainer entities, Note.TypeExamen typeExamen) {
        
        return switch (typeExamen) {
            case NORMAL -> processNormalExamNotes(noteList, entities);
            case RATTRAPAGE -> processRattrapageExamNotes(noteList, entities);
        };
    }

    private NoteProcessingResult processNormalExamNotes(List<EtudiantNoteDto> noteList, EntityContainer entities) {
        List<Note> notesToSave = new ArrayList<>();
        List<NoteFinal> finalGradesToSave = new ArrayList<>();

        for (EtudiantNoteDto noteDto : noteList) {
            if (entities.getModule().isHasTp()) {
                processNormalExamWithTp(noteDto, entities, notesToSave, finalGradesToSave);
            } else {
                processNormalExamWithoutTp(noteDto, entities, notesToSave, finalGradesToSave);
            }
        }

        return new NoteProcessingResult(notesToSave, finalGradesToSave);
    }

    private void processNormalExamWithoutTp(EtudiantNoteDto noteDto, EntityContainer entities,
            List<Note> notesToSave, List<NoteFinal> finalGradesToSave) {
        
        NoteFinal.StatutNote status = determineGradeStatus(noteDto.getNoteExam());
        
        Note note = createNote(entities.getEnseignant(), noteDto, entities.getModule(), 
            entities.getFiliere(), noteDto.getNoteExam(), entities.getAnneeUniversitaire(), 
            Note.TypeExamen.NORMAL, false, status);
        
        notesToSave.add(note);
        
        if (status == NoteFinal.StatutNote.VALIDE) {
            finalGradesToSave.add(createNoteFinal(note));
        }
    }

    private void processNormalExamWithTp(EtudiantNoteDto noteDto, EntityContainer entities,
            List<Note> notesToSave, List<NoteFinal> finalGradesToSave) {
        
        double averageGrade = calculateWeightedAverage(noteDto.getNoteExam(), noteDto.getNoteTp());
        NoteFinal.StatutNote status = determineGradeStatus(averageGrade);

        // Create exam note
        Note examNote = createNote(entities.getEnseignant(), noteDto, entities.getModule(),
            entities.getFiliere(), noteDto.getNoteExam(), entities.getAnneeUniversitaire(),
            Note.TypeExamen.NORMAL, false, status);

        // Create TP note
        Note tpNote = createNote(entities.getEnseignant(), noteDto, entities.getModule(),
            entities.getFiliere(), noteDto.getNoteTp(), entities.getAnneeUniversitaire(),
            Note.TypeExamen.NORMAL, true, status);

        notesToSave.addAll(Arrays.asList(examNote, tpNote));

        if (status == NoteFinal.StatutNote.VALIDE) {
            NoteFinal noteFinal = createNoteFinal(tpNote);
            noteFinal.setNote(averageGrade);
            finalGradesToSave.add(noteFinal);
        }
    }

    private NoteProcessingResult processRattrapageExamNotes(List<EtudiantNoteDto> noteList, EntityContainer entities) {
        List<Note> notesToSave = new ArrayList<>();
        List<NoteFinal> finalGradesToSave = new ArrayList<>();

        for (EtudiantNoteDto noteDto : noteList) {
            NoteFinal.StatutNote status = determineGradeStatus(noteDto.getNoteExam());

            Note note = createNote(entities.getEnseignant(), noteDto, entities.getModule(),
                entities.getFiliere(), noteDto.getNoteExam(), entities.getAnneeUniversitaire(),
                Note.TypeExamen.RATTRAPAGE, false, status);

            notesToSave.add(note);

            if (status == NoteFinal.StatutNote.VALIDE) {
                finalGradesToSave.add(createNoteFinal(note));
            } else {
                processFailedRattrapageNote(note, noteDto, entities, finalGradesToSave);
            }
        }

        return new NoteProcessingResult(notesToSave, finalGradesToSave);
    }

    private void processFailedRattrapageNote(Note note, EtudiantNoteDto noteDto, 
            EntityContainer entities, List<NoteFinal> finalGradesToSave) {
        
        List<Note> normalNotes = noteRepository
            .findByEtudiant_CodeApjAndModule_IdModuleAndSemesterAndAnneeUniversitaire_AnneeId(
                noteDto.getCodeApj(),
                entities.getModule().getIdModule(),
                entities.getModule().getSemester(),
                entities.getAnneeUniversitaire().getAnneeId());

        if (!normalNotes.isEmpty()) {
            double normalGrade = calculateNormalGrade(normalNotes, entities.getModule());
            
            NoteFinal noteFinal = createNoteFinal(note);
            noteFinal.setNote(Math.max(normalGrade, note.getNote()));
            finalGradesToSave.add(noteFinal);
        }
    }

    private void saveNotesInBatch(NoteProcessingResult result) {
        if (!result.getNotesToSave().isEmpty()) {
            noteRepository.saveAll(result.getNotesToSave());
        }
        if (!result.getFinalGradesToSave().isEmpty()) {
            noteFinalRepository.saveAll(result.getFinalGradesToSave());
        }
    }

    private NoteFinal.StatutNote determineGradeStatus(double grade) {
        return grade >= PASSING_GRADE ? NoteFinal.StatutNote.VALIDE : NoteFinal.StatutNote.NON_VALIDE;
    }

    private double calculateWeightedAverage(double examGrade, double tpGrade) {
        return examGrade * EXAM_WEIGHT + tpGrade * TP_WEIGHT;
    }

    private double calculateNormalGrade(List<Note> notes, Module module) {
        if (CollectionUtils.isEmpty(notes)) {
            throw new IllegalArgumentException("Notes list cannot be null or empty");
        }
        
        if (module.isHasTp()) {
            if (notes.size() < 2) {
                throw new IllegalArgumentException("Module has TP but less than 2 notes provided");
            }
            
            Map<Boolean, List<Note>> notesByType = notes.stream()
                .collect(Collectors.partitioningBy(Note::getEstTp));
            
            List<Note> examNotes = notesByType.get(false);
            List<Note> tpNotes = notesByType.get(true);
            
            if (examNotes.isEmpty() || tpNotes.isEmpty()) {
                throw new IllegalArgumentException("Missing exam or TP notes for module with TP");
            }
            
            return calculateWeightedAverage(examNotes.get(0).getNote(), tpNotes.get(0).getNote());
        }
        
        return notes.get(0).getNote();
    }

    private Note createNote(Enseignant enseignant, EtudiantNoteDto noteDto, Module module, 
            Filiere filiere, double noteValue, AnneeUniversitaire anneeUniversitaire,
            Note.TypeExamen typeExamen, boolean isTp, NoteFinal.StatutNote status) {
        
        Etudiant etudiant = etudiantRepository.findById(noteDto.getCodeApj())
            .orElseThrow(() -> new EtudiantNotFoundException(
                "Student not found with id=" + noteDto.getCodeApj()));

        Note note = new Note();
        note.setEnseignant(enseignant);
        note.setEtudiant(etudiant);
        note.setModule(module);
        note.setFiliere(filiere);
        note.setNote(noteValue);
        note.setSemester(module.getSemester());
        note.setAnneeUniversitaire(anneeUniversitaire);
        note.setDateNotation(LocalDate.now());
        note.setTypeExamen(typeExamen);
        note.setEstTp(isTp);
        note.setStatut(status);
        
        return note;
    }

    private NoteFinal createNoteFinal(Note note) {
        NoteFinal noteFinal = new NoteFinal();
        noteFinal.setEnseignant(note.getEnseignant());
        noteFinal.setEtudiant(note.getEtudiant());
        noteFinal.setModule(note.getModule());
        noteFinal.setFiliere(note.getFiliere());
        noteFinal.setNote(note.getNote());
        noteFinal.setSemester(note.getSemester());
        noteFinal.setAnneeUniversitaire(note.getAnneeUniversitaire());
        noteFinal.setStatut(note.getStatut());
        return noteFinal;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> getEtudiantNotesByYear(Integer idEtudiant) {
        if (Objects.isNull(idEtudiant)) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + idEtudiant));
        
        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        if (etudiant.getStatut() == Etudiant.StatutEtudiant.NORMAL||etudiant.getStatut() == Etudiant.StatutEtudiant.REDOUBLANT) {
            List<Module> modules=moduleRepository.findBySemesterAndFiliere_FiliereId(etudiant.getSemester(),etudiant.getFiliereID().getFiliereId());


            return noteRepository.findByEtudiant_CodeApjAndAnneeUniversitaire_AnneeId(
                idEtudiant, anneeUniversitaire.getAnneeId());
        }
        else if(etudiant.getStatut() == Etudiant.StatutEtudiant.REDOUBLANT_PARTIEL){
                List<Module> modules=moduleRepository.findBySemesterAndFiliere_FiliereId(etudiant.getSemester(),etudiant.getFiliereID().getFiliereId());


        }

        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "moduleGradeStatistics", key = "#moduleId")
    @Transactional(readOnly = true)
    public GradeStatistics getModuleGradeStatistics(Long moduleId) {
        log.debug("Calculating grade statistics for module {}", moduleId);
        
        moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + moduleId));

        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        List<NoteFinal> moduleNotes = noteFinalRepository.findByModule_IdModuleAndAnneeUniversitaire_AnneeId(moduleId,anneeUniversitaire.getAnneeId());
        
        if (moduleNotes.isEmpty()) {
            return GradeStatistics.empty();
        }

        DoubleSummaryStatistics stats = moduleNotes.stream()
            .mapToDouble(NoteFinal::getNote)
            .summaryStatistics();

        long passedCount = moduleNotes.stream()
            .mapToLong(note -> note.getNote() >= PASSING_GRADE ? 1 : 0)
            .sum();

        long failedCount = moduleNotes.size() - passedCount;
        double passRate = (double) passedCount / moduleNotes.size() * 100;

        return new GradeStatistics(
            stats.getAverage(),
            stats.getMax(),
            stats.getMin(),
            passedCount,
            failedCount,
            passRate
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeHistory> getStudentGradeHistory(Integer studentId, Long moduleId) {
        moduleRepository.findById(moduleId)
            .orElseThrow(() -> new ModuleNotFoundException("Module not found with id=" + moduleId));

        List<Note> notes = noteRepository
            .findByEtudiant_CodeApjAndModule_IdModuleOrderByDateNotationDesc(studentId, moduleId);

        return notes.stream()
            .map(note -> new GradeHistory(
                note.getNote(),
                note.getTypeExamen(),
                note.getDateNotation(),
                note.getStatut(),
                note.getEstTp()
            ))
            .collect(Collectors.toList());
    }

    public Map<String, Map<String, Double>> getEtudiantNote(Integer codApj){

        Etudiant etudiant = etudiantRepository.findById(codApj).orElseThrow(()->new EtudiantNotFoundException("Etudiant not found with the id=" + codApj));

        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        List<Module> modules = moduleRepository.findBySemesterAndFiliere_FiliereIdAndAnneeUniversitaire_AnneeId(etudiant.getSemester(), etudiant.getFiliereID().getFiliereId(),anneeUniversitaire.getAnneeId());

        return mapToModuleAndNoteForNormalAndRedoublant(modules,etudiant,anneeUniversitaire);
    }

    private Map<String, Map<String, Double>> mapToModuleAndNoteForNormalAndRedoublant(List<Module> modules, Etudiant etudiant,AnneeUniversitaire anneeUniversitaire) {
        Map<String, Map<String, Double>> moduleAndNote = new HashMap<>();
        modules.forEach(m->moduleAndNote.put(m.getNomModule(),null));

        List<Long> moduleAffichIds = moduleAffichRepository.ModuleAfficheNormal(anneeUniversitaire.getAnneeId());

        for (Module module : modules) {
            if (moduleAffichIds.contains(module.getIdModule())) {
                List<Note> notes = noteRepository
                        .findNotesByStudentModuleSemesterAndYear(
                                etudiant.getCodeApj(),
                                module.getIdModule(),
                                module.getSemester(),
                                anneeUniversitaire.getAnneeId());

                Map<String, Double> noteMap = new HashMap<>();
                
                // Process notes and organize by type
                for (Note note : notes) {
                    String key = note.getTypeExamen().toString() + (note.getEstTp() ? "_TP" : "_EXAM");
                    noteMap.put(key, note.getNote());
                }

                // If module doesn't have TP, copy normal exam note to TP
                if (!module.isHasTp() && noteMap.containsKey("NORMAL_EXAM")) {
                    noteMap.put("NORMAL_TP", noteMap.get("NORMAL_EXAM"));
                }

                if (!noteMap.isEmpty()) {
                    moduleAndNote.put(module.getNomModule(), noteMap);
                }
            }
        }

        return moduleAndNote;
    }
    @Override
    public Note validateAndCorrectGrade(Note note) {
        validateGradeRange(note.getNote());

        if (isSuspiciousGrade(note)) {
            double correctedGrade = validateSuspiciousGrade(note);
            log.warn("Corrected suspicious grade from {} to {} for student {} in module {}", 
                note.getNote(), correctedGrade, note.getEtudiant().getCodeApj(), 
                note.getModule().getIdModule());
            note.setNote(correctedGrade);
        }

        note.setStatut(determineGradeStatus(note.getNote()));
        return note;
    }

    private void validateGradeRange(double grade) {
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException(
                String.format("Grade must be between %.1f and %.1f", MIN_GRADE, MAX_GRADE));
        }
    }

    private boolean isSuspiciousGrade(Note note) {
        OptionalDouble moduleAverageOpt = noteRepository.findByModule_IdModule(note.getModule().getIdModule())
            .stream()
            .mapToDouble(Note::getNote)
            .average();

        if (moduleAverageOpt.isEmpty()) {
            return false;
        }

        double moduleAverage = moduleAverageOpt.getAsDouble();
        return Math.abs(note.getNote() - moduleAverage) > SUSPICIOUS_GRADE_THRESHOLD;
    }

    private double validateSuspiciousGrade(Note note) {
        List<Note> moduleNotes = noteRepository.findByModule_IdModule(note.getModule().getIdModule());
        
        if (moduleNotes.size() < 2) {
            return note.getNote(); // Not enough data for statistical validation
        }

        DoubleSummaryStatistics stats = moduleNotes.stream()
            .mapToDouble(Note::getNote)
            .summaryStatistics();

        double average = stats.getAverage();
        double stdDev = calculateStandardDeviation(moduleNotes, average);

        double threshold = STANDARD_DEVIATION_MULTIPLIER * stdDev;
        
        if (Math.abs(note.getNote() - average) > threshold) {
            return note.getNote() > average ? 
                Math.min(average + threshold, MAX_GRADE) : 
                Math.max(average - threshold, MIN_GRADE);
        }

        return note.getNote();
    }

    private double calculateStandardDeviation(List<Note> notes, double mean) {
        return Math.sqrt(notes.stream()
            .mapToDouble(note -> Math.pow(note.getNote() - mean, 2))
            .average()
            .orElse(0.0));
    }

    @Override
    @Transactional
    public double getStudentGPA(Integer studentId, Module.Semester semester) {
        log.info("Calculating GPA for student {} in semester {}", studentId, semester);
        
        Etudiant etudiant = etudiantRepository.findById(studentId)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + studentId));

        // Advance semester if in odd-numbered semester
        Module.Semester targetSemester = switch (semester) {
            case S1 -> Module.Semester.S2;
            case S3 -> Module.Semester.S4;
            case S5 -> Module.Semester.S6;
            case S7 -> Module.Semester.S8;
            default -> semester;
        };

        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        // Get all modules for the student's filiere in the target semester
        List<Module> modules = moduleRepository.findBySemesterAndFiliere_FiliereId(targetSemester, etudiant.getFiliereID().getFiliereId());
        
        // Get displayed modules
        List<Long> displayedModuleIds = moduleAffichRepository.ModuleAfficheNormal(currentYear.getAnneeId());
        
        // Filter modules that are displayed
        List<Module> displayedModules = modules.stream()
            .filter(module -> displayedModuleIds.contains(module.getIdModule()))
            .collect(Collectors.toList());
            
        if (displayedModules.isEmpty()) {
            return 0.0;
        }

        // Calculate average for each module
        List<Double> moduleAverages = new ArrayList<>();
        for (Module module : displayedModules) {
            // For REDOUBLANT_PARTIEL and RESERVISTE, check if module was valid in previous year
            if ((etudiant.getStatut() == Etudiant.StatutEtudiant.REDOUBLANT_PARTIEL || 
                 etudiant.getStatut() == Etudiant.StatutEtudiant.RESERVISTE)) {
                
                // Get previous year's note for this module
                List<NoteFinal> previousYearNotes = noteFinalRepository
                    .findByEtudiant_CodeApjAndModule_IdModuleAndStatut(
                        studentId, module.getIdModule(), NoteFinal.StatutNote.VALIDE);
                
                if (!previousYearNotes.isEmpty()) {
                    // If module was valid in previous year, use that grade
                    moduleAverages.add(previousYearNotes.get(0).getNote());
                    continue;
                }
            }
            
            // If not valid in previous year or not REDOUBLANT_PARTIEL/RESERVISTE, calculate current year's grade
            List<Note> notes = noteRepository
                .findNotesByStudentModuleSemesterAndYear(
                    studentId, module.getIdModule(), targetSemester, currentYear.getAnneeId());
                    
            if (!notes.isEmpty()) {
                double moduleAverage = calculateNormalGrade(notes, module);
                moduleAverages.add(moduleAverage);
            }
        }

        if (moduleAverages.isEmpty()) {
            return 0.0;
        }

        // Calculate semester average
        double semesterAverage = moduleAverages.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        // Check for compensation
        boolean canCompensate = moduleAverages.stream()
            .filter(avg -> avg < PASSING_GRADE)
            .count() <= 2; // Allow compensation if at most 2 modules are below passing grade

        // Create or update semester note
        SemesterNote semesterNote = semesterNoteRepository
            .findByEtudiant_CodeApjAndSemesterAndAnneeUniversitaire_AnneeId(studentId, targetSemester, currentYear.getAnneeId())
            .orElse(SemesterNote.builder()
                .etudiant(etudiant)
                .semester(targetSemester)
                .anneeUniversitaire(currentYear)
                .build());

        semesterNote.setNote(semesterAverage);
        
        // Set status based on average and compensation
        if (semesterAverage >= PASSING_GRADE) {
            semesterNote.setStatut(SemesterNote.StatutSemester.VALIDE);
        } else if (canCompensate) {
            semesterNote.setStatut(SemesterNote.StatutSemester.VALIDE);
            // Update failed module statuses to ACQUIS_PAR_COMPENSATION
            for (Module module : displayedModules) {
                List<NoteFinal> failedNotes = noteFinalRepository
                    .findByEtudiant_CodeApjAndModule_IdModuleAndStatutAndAnneeUniversitaire_AnneeId(
                        studentId, module.getIdModule(), NoteFinal.StatutNote.NON_VALIDE, currentYear.getAnneeId());
                        
                for (NoteFinal note : failedNotes) {
                    note.setStatut(NoteFinal.StatutNote.ACQUIS_PAR_COMPENSATION);
                }
                noteFinalRepository.saveAll(failedNotes);
            }
        } else {
            semesterNote.setStatut(SemesterNote.StatutSemester.NON_VALIDE);
        }

        semesterNoteRepository.save(semesterNote);
        
        return semesterAverage;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentStatistics getStudentStatistics(Integer studentId) {
        Etudiant etudiant = etudiantRepository.findById(studentId)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + studentId));

        List<NoteFinal> allNotes = noteFinalRepository.findByEtudiant_CodeApj(studentId);
        
        Map<Boolean, Long> passFailCount = allNotes.stream()
            .collect(Collectors.partitioningBy(
                note -> note.getNote() >= PASSING_GRADE,
                Collectors.counting()
            ));
        
        long passedModules = passFailCount.get(true);
        long failedModules = passFailCount.get(false);
        
        double gpa = calculateStudentGPA(studentId, etudiant.getSemester());
        double attendanceRate = attendanceService.getStudentAttendanceRate(studentId);
        
        return new StudentStatistics(
            gpa,
            (int) (passedModules + failedModules),
            (int) passedModules,
            (int) failedModules,
            attendanceRate,
            etudiant.getSemester().toString(),
            etudiant.getStatut().toString()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<noteDto> getStudentModuleNotes(Integer studentId, Long moduleId) {

        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        Module module = moduleRepository.findById(moduleId).orElseThrow(()->new ModuleNotFoundException("Module Note found with id="+moduleId));
        return noteRepository.findNotesByStudentModuleSemesterAndYear(
            studentId, moduleId,module.getSemester(),anneeUniversitaire.getAnneeId()).stream().map(this::mapTNoteDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<noteDto> getModuleNotes(Long moduleId) {

        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        return noteRepository.findByModule_IdModuleAndAndAnneeUniversitaire_AnneeIdOrderByDateNotationDesc(moduleId,anneeUniversitaire.getAnneeId()).stream().map(this::mapTNoteDto).toList();
    }

    @Override
    public void updateNote(Integer noteId, double newGrade) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new NoteNotFoundException("Note not found with id=" + noteId));
        
        validateGradeRange(newGrade);
        
        double oldGrade = note.getNote();
        note.setNote(newGrade);
        note.setStatut(determineGradeStatus(newGrade));
        
        noteRepository.save(note);
        
        log.info("Updated note {} from {} to {} for student {}", 
            noteId, oldGrade, newGrade, note.getEtudiant().getCodeApj());
    }

    @Override
    public void deleteNote(Integer noteId) {
        if (!noteRepository.existsById(noteId)) {
            throw new NoteNotFoundException("Note not found with id=" + noteId);
        }
        
        noteRepository.deleteById(noteId);
        log.info("Deleted note with id {}", noteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteFinal> getStudentFailedModules(Integer studentId) {
        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        return noteFinalRepository.findByEtudiant_CodeApjAndStatutAndAnneeUniversitaire_AnneeId(
            studentId, NoteFinal.StatutNote.NON_VALIDE,anneeUniversitaire.getAnneeId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteFinal> getStudentPassedModules(Integer studentId) {
        AnneeUniversitaire anneeUniversitaire = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
                .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        List<NoteFinal> passedModules=noteFinalRepository.findByEtudiant_CodeApjAndStatutAndAnneeUniversitaire_AnneeId(
            studentId, NoteFinal.StatutNote.VALIDE,anneeUniversitaire.getAnneeId());

        passedModules.addAll(noteFinalRepository.findByEtudiant_CodeApjAndStatutAndAnneeUniversitaire_AnneeId(
                studentId, NoteFinal.StatutNote.ACQUIS_PAR_COMPENSATION, anneeUniversitaire.getAnneeId()));

        return passedModules;
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateStudentGPA(Integer studentId, Module.Semester semester) {
        return getStudentGPA(studentId, semester);
    }

    @Override
    @Transactional
    public void updateStudentStatus(Integer studentId) {
        Etudiant etudiant = etudiantRepository.findById(studentId)
            .orElseThrow(() -> new EtudiantNotFoundException("Student not found with id=" + studentId));

        Module.Semester currentSemester = etudiant.getSemester();
        
        // Only process even-numbered semesters
        if (currentSemester != Module.Semester.S2 && 
            currentSemester != Module.Semester.S4 && 
            currentSemester != Module.Semester.S6 && 
            currentSemester != Module.Semester.S8) {
            return;
        }

        AnneeUniversitaire currentYear = anneeUniversitaireRepository.findByAnneeCouranteIsTrue()
            .orElseThrow(() -> new AnneeUniversitaireNotFoundException("Current academic year not found"));

        // Get all modules for the current semester
        List<Module> modules = moduleRepository.findBySemesterAndFiliere_FiliereId(
            currentSemester, etudiant.getFiliereID().getFiliereId());

        // Count valid and compensated modules
        long validModules = noteFinalRepository.countByEtudiant_CodeApjAndStatutInAndAnneeUniversitaire_AnneeId(
            studentId, 
            Arrays.asList(NoteFinal.StatutNote.VALIDE, NoteFinal.StatutNote.ACQUIS_PAR_COMPENSATION),
            currentYear.getAnneeId()
        );

        // Update status based on number of valid modules
        if (validModules == 0) {
            etudiant.setStatut(Etudiant.StatutEtudiant.REDOUBLANT);
        } else if (validModules < 3) {
            etudiant.setStatut(Etudiant.StatutEtudiant.REDOUBLANT_PARTIEL);
        } else if (validModules < modules.size()) {
            // Check if this is the last semester in the filiere
            FiliereSemester filiereSemester = filiereSemesterRepository
                .findByFiliere_FiliereIdAndSemester(etudiant.getFiliereID().getFiliereId(), currentSemester)
                .orElseThrow(() -> new RuntimeException("Filiere semester configuration not found"));
            
            if (!filiereSemester.isLastSemester()) {
                // Advance to next semester
                Module.Semester nextSemester = switch (currentSemester) {
                    case S2 -> Module.Semester.S3;
                    case S4 -> Module.Semester.S5;
                    case S6 -> Module.Semester.S7;
                    case S8 -> Module.Semester.S9;
                    default -> currentSemester;
                };
                etudiant.setSemester(nextSemester);
            }
            
            // Check if any semester is non-valid
            boolean hasFailedSemester = semesterNoteRepository.existsByEtudiant_CodeApjAndStatut(
                studentId, SemesterNote.StatutSemester.NON_VALIDE);
            
            etudiant.setStatut(hasFailedSemester ? 
                Etudiant.StatutEtudiant.RESERVISTE : 
                Etudiant.StatutEtudiant.VALIDE_NIVEAU);
        }

        etudiantRepository.save(etudiant);
        log.info("Updated status for student {} to {}", studentId, etudiant.getStatut());
    }

    // Helper classes
    private static class EntityContainer {
        private final Module module;
        private final AnneeUniversitaire anneeUniversitaire;
        private final Filiere filiere;
        private final Enseignant enseignant;

        public EntityContainer(Module module, AnneeUniversitaire anneeUniversitaire, 
                Filiere filiere, Enseignant enseignant) {
            this.module = module;
            this.anneeUniversitaire = anneeUniversitaire;
            this.filiere = filiere;
            this.enseignant = enseignant;
        }

        // Getters
        public Module getModule() { return module; }
        public AnneeUniversitaire getAnneeUniversitaire() { return anneeUniversitaire; }
        public Filiere getFiliere() { return filiere; }
        public Enseignant getEnseignant() { return enseignant; }
    }

    private static class NoteProcessingResult {
        private final List<Note> notesToSave;
        private final List<NoteFinal> finalGradesToSave;

        public NoteProcessingResult(List<Note> notesToSave, List<NoteFinal> finalGradesToSave) {
            this.notesToSave = notesToSave;
            this.finalGradesToSave = finalGradesToSave;
        }

        // Getters
        public List<Note> getNotesToSave() { return notesToSave; }
        public List<NoteFinal> getFinalGradesToSave() { return finalGradesToSave; }
    }

    private void sendNoteNotifications(List<Note> notes, Module module) {
        for (Note note : notes) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(note.getEtudiant().getUserId().getEmail());
            message.setSubject("New Grade Posted - " + module.getNomModule());
            message.setText(String.format(
                "Dear %s %s,\n\n" +
                "Your grade for %s has been posted.\n\n" +
                "Grade Details:\n" +
                "Module: %s\n" +
                "Grade: %.2f\n" +
                "Type: %s\n" +
                "Exame Type:%s\n"+
                "Status: %s\n\n" +
                "Best regards,\n" +
                "School Management System",
                note.getEtudiant().getPrenom(),
                note.getEtudiant().getNom(),
                module.getNomModule(),
                module.getNomModule(),
                note.getNote(),
                note.getTypeExamen(),
                note.getEstTp()? "TP":"EXAM",
                note.getStatut()
            ));
            
            try {
                mailSender.send(message);
                log.info("Sent grade notification email to student {}", note.getEtudiant().getCodeApj());
            } catch (Exception e) {
                log.error("Failed to send grade notification email to student {}", 
                    note.getEtudiant().getCodeApj(), e);
            }
        }
    }

    private noteDto mapTNoteDto(Note note){
        return new noteDto(note.getIdNote(),note.getNote(),note.getEstTp()?"TP":"Exam",note.getTypeExamen());
    }
}