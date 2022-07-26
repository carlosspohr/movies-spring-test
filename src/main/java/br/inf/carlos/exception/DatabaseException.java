package br.inf.carlos.exception;

@SuppressWarnings("serial")
public class DatabaseException extends Exception{

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

}
