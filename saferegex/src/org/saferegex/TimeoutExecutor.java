package org.saferegex;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TimeoutExecutor {
    private final ExecutorService service;
    private final long tiemout;
    private final TimeUnit unit;
    
    public TimeoutExecutor(ExecutorService service,
            long timeout,
            TimeUnit unit) {
        this.service = service;
        this.tiemout = timeout;
        this.unit = unit;
    }
    
    public TimeoutExecutor() {
        this(Executors.newFixedThreadPool(1), 1, TimeUnit.SECONDS);
    }
    
    public <T> T execute(Callable<T> task, ExceptionMapper handler) {
        Future<T> result = service.submit(task);
        try {
            return result.get(tiemout, unit);
        } catch(Exception e) {
            throw handler.map(e);
        }
    }
    
    public void shutDown() {
        this.service.shutdown();
    }
    
    public static interface ExceptionMapper {
        public RuntimeException map(Exception e);
    }
}
