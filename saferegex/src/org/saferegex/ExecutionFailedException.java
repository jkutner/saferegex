package org.saferegex;


public class ExecutionFailedException extends Exception {
    private static final long serialVersionUID = 1L;

    public ExecutionFailedException(Exception e) {
        super(e);
    }
}
