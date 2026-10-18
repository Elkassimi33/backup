package com.FSTS.schoolManager.exception;

public class FileExistsException extends RuntimeException{

    public FileExistsException(String message){
        super(message);
    }
}
