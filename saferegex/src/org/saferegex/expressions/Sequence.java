package org.saferegex.expressions;

import java.util.HashSet;
import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class Sequence implements NestedExpression {
    private final Expression head;
    private final Expression tail;
    
    public static Sequence of(Expression a, Expression b) {
        return new Sequence(a, b);
    }
    
    public Sequence(Expression a, Expression b) {
        this.head = a.simplify();
        this.tail = b.simplify();
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }
    
    @Override
    public Expression intersect(Expression other) {
        return head.intersect(other);
    }

    @Override
    public Match matchAt(int i, char c) {
        Match matchA = head.matchAt(i, c);
        if(!matchA.equals(Match.OUT_OF_RANGE)) {
            return matchA;
        } else {
            Match bestMatch = Match.OUT_OF_RANGE;
            for(int length: head.lengthOptions()) {
                Match match = tail.matchAt(i - length, c);
                switch (match) {
                    case MATCH:
                        return Match.MATCH;
                    case NO_MATCH:
                        bestMatch = Match.NO_MATCH;                    
                }
            }
            return bestMatch;
        }
    }

    @Override
    public Set<Integer> lengthOptions() {
        Set<Integer> options = new HashSet<Integer>();
        for(int lengthA: head.lengthOptions()) {
            for(int lengthB: tail.lengthOptions()) {
                options.add(lengthA + lengthB);
            }
        }
        return options;
    }

    @Override
    public String toString() {
        return head.toString() + tail.toString();
    }

    @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.sequence(
            head.samples(strategy),
            tail.samples(strategy));
    }

    @Override
    public Expression simplify() {
        if(head.isEmpty()) {
            return tail;
        } else if(tail.isEmpty()) {
            return head;
        } else if(head instanceof Atom && tail instanceof Atom) {
            return Atom.of(head.toString() + tail.toString());
        } else {
            return this;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
        
        if(obj instanceof Sequence) {
            Sequence other = (Sequence)obj;
            return head.equals(other.head) && tail.equals(other.tail);
        } else {
            Expression self = simplify();
            if(self instanceof Sequence) {
                return false;
            } else {
                return self.equals(obj);
            }
        }
    }
    
    @Override
    public int hashCode() {
        Expression self = simplify();
        if(self instanceof Sequence) {
            final int prime = 31;
            int result = 1;
            result = prime * result + head.hashCode();
            result = prime * result + tail.hashCode();
            return result;
        } else {
            return self.hashCode();
        }
    }
}
