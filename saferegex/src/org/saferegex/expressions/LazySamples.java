package org.saferegex.expressions;

import java.util.Iterator;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class LazySamples implements Samples {
    private final SamplingStrategy strategy;
    private final Expression expression;
    private Samples samples = null;
    
    public static Samples of(SamplingStrategy strategy,
            Expression expression) {
        return new LazySamples(strategy, expression);
    }
    
    public LazySamples(SamplingStrategy strategy,
            Expression expression) {
        this.strategy = strategy;
        this.expression = expression;
    }

    public void force() {
        if(samples == null) {
            samples = expression.samples(strategy);
        }
    }
    
    @Override
    public boolean isEmpty() {
        force();
        return samples.isEmpty();
    }

    @Override
    public int size() {
        force();
        return samples.size();
    }

    @Override
    public Iterator<String> iterator() {
        force();
        return samples.iterator();
    }

    @Override
    public String toString() {
        force();
        return samples.toString();
    }
}
