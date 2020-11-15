package com.gmart.api.core.exceptions;

public class FileStorageException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7996856200822195243L;

	private String message;
	
	public FileStorageException(){
		super();
	}
	public FileStorageException(String message){
	      super(message);
	      this.message = message;

	}
	public FileStorageException(String message, Exception e ){
		      super(message, e);
		      this.message = message;
	}
 		    		
 public String getMessage(){
 return this.message;
  }
}
