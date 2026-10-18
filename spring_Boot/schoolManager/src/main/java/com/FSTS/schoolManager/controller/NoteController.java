package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.dtos.EtudiantNoteDto;
import com.FSTS.schoolManager.dtos.GradeStatistics;
import com.FSTS.schoolManager.dtos.StudentStatistics;
import com.FSTS.schoolManager.dtos.noteDto;
import com.FSTS.schoolManager.entities.Note;
import com.FSTS.schoolManager.entities.NoteFinal;
import com.FSTS.schoolManager.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Void> insertNotes(
            @RequestBody List<EtudiantNoteDto> noteList,
            @RequestParam Long idModule,
            @RequestParam Integer idEnseignant,
            @RequestParam Integer idFiliere,
            @RequestParam Note.TypeExamen typeExamen) {
        noteService.insertNotes(noteList, idModule, idEnseignant, idFiliere, typeExamen);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{idEtudiant}")
    public ResponseEntity<List<Note>> getEtudiantNotesByYear(@PathVariable Integer idEtudiant) {
        return ResponseEntity.ok(noteService.getEtudiantNotesByYear(idEtudiant));
    }

    @GetMapping("/module/{moduleId}/statistics")
    public ResponseEntity<GradeStatistics> getModuleGradeStatistics(@PathVariable Long moduleId) {
        return ResponseEntity.ok(noteService.getModuleGradeStatistics(moduleId));
    }

    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<StudentStatistics> getStudentStatistics(@PathVariable Integer studentId) {
        return ResponseEntity.ok(noteService.getStudentStatistics(studentId));
    }

    @GetMapping("/student/{studentId}/module/{moduleId}")
    public ResponseEntity<List<noteDto>> getStudentModuleNotes(
            @PathVariable Integer studentId,
            @PathVariable Long moduleId) {
        return ResponseEntity.ok(noteService.getStudentModuleNotes(studentId, moduleId));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<noteDto>> getModuleNotes(@PathVariable Long moduleId) {
        return ResponseEntity.ok(noteService.getModuleNotes(moduleId));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<Void> updateNote(
            @PathVariable Integer noteId,
            @RequestParam double newGrade) {
        noteService.updateNote(noteId, newGrade);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Integer noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}/failed")
    public ResponseEntity<List<NoteFinal>> getStudentFailedModules(@PathVariable Integer studentId) {
        return ResponseEntity.ok(noteService.getStudentFailedModules(studentId));
    }

    @GetMapping("/student/{studentId}/passed")
    public ResponseEntity<List<NoteFinal>> getStudentPassedModules(@PathVariable Integer studentId) {
        return ResponseEntity.ok(noteService.getStudentPassedModules(studentId));
    }

    @GetMapping("/student/{studentId}/notes")
    public ResponseEntity<Map<String, Map<String, Double>>> getEtudiantNote(@PathVariable Integer studentId) {
        return ResponseEntity.ok(noteService.getEtudiantNote(studentId));
    }
} 