package com.FSTS.schoolManager.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EtudiantNotFoundException.class)
    public ProblemDetail handelEtudiantNotFoundException(EtudiantNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(EmptyFileException.class)
    public ProblemDetail handelEmptyFileException(EmptyFileException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(FileExistsException.class)
    public ProblemDetail handelFileExistsException(FileExistsException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(FiliereNotFoundException.class)
    public ProblemDetail handelFiliereNotFoundException(FiliereNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(EnseignantNotFoundException.class)
    public ProblemDetail handelEnseignantNotFoundException(EnseignantNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(DepartementNotFoundException.class)
    public ProblemDetail handelDepartementNotFoundException(DepartementNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(AnneeUniversitaireNotFoundException.class)
    public ProblemDetail handelAnneeUniversitaireNotFoundException(AnneeUniversitaireNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ProblemDetail handelModuleNotFoundException(ModuleNotFoundException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handelIllegalArgumentException(IllegalArgumentException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ProblemDetail handelJwtExpiredException(JwtExpiredException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ProblemDetail handelInvalidJwtException(InvalidJwtException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ProblemDetail handelRefreshTokenException(RefreshTokenException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
}
