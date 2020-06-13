package com.fundoo.project.exception;

public class UserException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public String message;
    public UserException(String message){
        this.message=message;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public String getMessage(){
        return this.message;
    }
}
