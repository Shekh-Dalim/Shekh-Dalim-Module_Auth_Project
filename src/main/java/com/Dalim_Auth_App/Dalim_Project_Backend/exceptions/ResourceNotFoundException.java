package com.Dalim_Auth_App.Dalim_Project_Backend.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    // TODO Take the error message you provide and pass it to the parent Exception class.
    public ResourceNotFoundException(String message){
        super(message);  // TODO Call the parent class (RuntimeException) constructor and pass the message to it.
    }

    public ResourceNotFoundException(){   // TODO if we no pass any message inside the constructor then we run this method
        super("Resource not found !!!");
    }
}
