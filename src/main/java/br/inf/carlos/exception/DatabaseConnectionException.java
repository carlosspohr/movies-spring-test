package br.inf.carlos.exception;

@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception{

	public DatabaseConnectionException(String message) {
		super(message);
	}

	public DatabaseConnectionException(Throwable cause) {
		super(cause);
	}

}
