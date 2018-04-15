package org.saferegex;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class TimeoutMatcher {
    private final TimeoutExecutor executor;
    private final Pattern pattern;
    private static final ExceptionMapper mapper =
        new ExceptionMapper();
    
    public TimeoutMatcher(TimeoutExecutor executor,
            Pattern pattern) {
        this.executor = executor;
        this.pattern = pattern;
    }

    public boolean match(final String value) {
        return executor.execute(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call()
                            throws Exception {
                        return pattern.matcher(value).matches();
                    }
                }, mapper);
    }
    
    protected static class ExceptionMapper
            implements TimeoutExecutor.ExceptionMapper {
        @Override
        public RuntimeException map(Exception e) {
            if(e instanceof ExecutionException) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof Error) {
                    throw (Error) cause;
                } else {
                    throw new IllegalStateException(cause);
                }
            } else {
                throw new IllegalStateException(e);
            }
        }
    }
}
