package org.saferegex.expressions;

import java.util.HashSet;
import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class Optional implements NestedExpression {
    private final Expression expression;
    
    public static Optional term(Expression expression) {
        return new Optional(expression);
    }
    
    public Optional(Expression expression) {
        Expression e = expression; 
        if(e instanceof Optional) {
            Optional optional = (Optional)e;
            e = optional.expression;
        }
        this.expression = e.simplify();
    }

    @Override
    public Expression intersect(Expression other) {
        return expression.intersect(other);
    }

    @Override
    public boolean isEmpty() {
        return expression.isEmpty();
    }

    @Override
    public Match matchAt(int i, char c) {
        return expression.matchAt(i, c);
    }

    @Override
    public Set<Integer> lengthOptions() {
        Set<Integer> options = new HashSet<Integer>();
        options.addAll(expression.lengthOptions());
        options.add(0);
        return options;
    }

    @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.optional(expression.samples(strategy));
    }

    @Override
    public Expression simplify() {
        if(expression.isEmpty()) {
            return expression;
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return String.format("(%s)?", 
            expression.toString());
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
        
        if(obj instanceof Optional) {
            Optional other = (Optional)obj;
            return expression.equals(other.expression);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return -expression.hashCode();
    }
}
