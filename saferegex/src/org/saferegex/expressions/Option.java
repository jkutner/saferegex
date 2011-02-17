package org.saferegex.expressions;

import java.util.HashSet;
import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class Option implements NestedExpression {
    private final Expression a;
    private final Expression b;
    
    public static Option of(Expression a, Expression b) {
        return new Option(a, b);
    }
    
    public Option(Expression a, Expression b) {
        this.a = a.simplify();
        this.b = b.simplify();
    }

    @Override
    public boolean isEmpty() {
        return a.isEmpty() && b.isEmpty();
    }

    @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.option(a.samples(strategy), 
            b.samples(strategy));
    }

    @Override
    public Expression intersect(Expression other) {
        return Option.of(a.intersect(other), 
            b.intersect(other)).simplify();
    }

    @Override
    public Match matchAt(int i, char c) {
        return a.matchAt(i, c).equals(Match.MATCH) ?
                Match.MATCH : b.matchAt(i, c);
    }

    @Override
    public Set<Integer> lengthOptions() {
        Set<Integer> set = new HashSet<Integer>();
        set.addAll(a.lengthOptions());
        set.addAll(b.lengthOptions());        
        return set;
    }

    @Override
    public String toString() {
        return String.format("(%s|%s)", 
            a.toString(), b.toString());
    }
    
    @Override
    public Expression simplify() {
        if(a.isEmpty()) {
            return b;
        } else if(b.isEmpty()) {
            return a;
        } if(a.equals(b)) {
            return a;
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
        
        if(obj instanceof Option) {
            Option other = (Option)obj;
            return a.equals(other.a) && b.equals(other.b);
        } else {
            Expression self = simplify();
            if(self instanceof Option) {
                return false;
            } else {
                return self.equals(obj);
            }
        }
    }
    
    @Override
    public int hashCode() {
        Expression self = simplify();
        if(self instanceof Option) {
            final int prime = 31;
            int result = 1;
            result = prime * result + a.hashCode();
            result = prime * result + b.hashCode();
            return result;
        } else {
            return self.hashCode();
        }
    }
}
