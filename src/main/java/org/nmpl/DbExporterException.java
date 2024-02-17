package org.nmpl;

public class DbExporterException extends RuntimeException {

    public DbExporterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public void printStackTrace() {
        System.err.println("Application Error : DbExporterException: " + getMessage());
    }
}