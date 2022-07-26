package br.inf.carlos.exception;

@SuppressWarnings("serial")
public class DataImportException extends Exception{

	public DataImportException(String message) {
		super(message);
	}

	public DataImportException(Throwable cause) {
		super(cause);
	}

}
