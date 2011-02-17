package org.saferegex.expressions;

import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class RecursiveRepetition implements Repetition {
    public static final int MAX_REPETITION = 30;
    
    private final Expression expression;
    private final int start;
    private final int end;
    private final Expression substitution;
    
    public static RecursiveRepetition of(Expression expression, int size) {
        return new RecursiveRepetition(expression, size, size);
    }
    
    public static RecursiveRepetition of(Expression expression, int start, int end) {
        return new RecursiveRepetition(expression, start, end);
    }

    public RecursiveRepetition(Expression expression, int start, int end) {
        this.expression = expression;
        this.start = start;
        this.end = end;
        substitution = substitute(expression, start, end);
    }

    protected Expression substitute(Expression expression, int start,
            int end) {
        end = Math.min(end, MAX_REPETITION);
        expression = expression.simplify();
        if(end < start) {
            throw new IllegalArgumentException();
        } else if(end == start) {
            return sequence(expression, start);
        } else if(start == 0) {
            return Optional.term(options(expression, end - start)).simplify();
        } else {
            return Sequence.of(sequence(expression, start - 1), 
                    options(expression, end - start + 1)).simplify();
        }
    }
    
    protected static Expression sequence(Expression expression, int times) {
        Expression p = Atom.EMPTY;
        for(int i = 0; i < times; ++i) {
            p = Sequence.of(p, expression);
        }
        return p.simplify();
    }
    
    protected static Expression options(Expression expression, int times) {
        Expression p = Atom.EMPTY;
        for(int i = times; i >=0; --i) {
            p = Option.of(sequence(expression, i), p);
        }
        return p.simplify();
    }
    
    @Override
    public Expression intersect(Expression other) {
        return substitution.intersect(other);
    }

    @Override
    public boolean isEmpty() {
        return substitution.isEmpty();
    }

    @Override
    public Match matchAt(int i, char c) {
        return substitution.matchAt(i, c);
    }

    @Override
    public Set<Integer> lengthOptions() {
        return substitution.lengthOptions();
    }

    @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.repetition(
            LazySamples.of(strategy, expression), 
            LazySamples.of(strategy, substitution),
                start, end);
    }

    @Override
    public String toString() {
        if(end == Integer.MAX_VALUE) {
            return String.format("(%s){%d,}", 
                expression, start);
        } else if(start == end ) {
            return String.format("(%s){%d}", 
                expression, start);
        } else {
            return String.format("(%s){%d,%d}", 
                    expression, start, end);
        }
    }
    
    public Expression substitution() {
        return substitution;
    }

    @Override
    public Expression simplify() {
        if(expression.isEmpty()) {
            return expression;
        }        
        return this;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
        
        if(obj instanceof RecursiveRepetition) {
            RecursiveRepetition other = (RecursiveRepetition)obj;
            return expression.equals(other.expression) &&
                start == other.start &&
                end == other.end;
        } else {
            Expression self = substitution;
            return self.equals(obj);
        }
    }
    
    @Override
    public int hashCode() {
        Expression self = simplify();
        if(self instanceof RecursiveRepetition) {
            final int prime = 31;
            int result = 1;
            result = prime * result + expression.hashCode();
            result = prime * result + start;
            result = prime * result + end;
            return result;
        } else {
            return self.hashCode();
        }
    }
}
