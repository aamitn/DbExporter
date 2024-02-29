package org.nmpl.v0;

public class DbExporterException extends RuntimeException {

    public DbExporterException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public void printStackTrace() {
        System.err.println("Application Error : DbExporterException: " + getMessage());
    }
}