package org.saferegex.expressions;

import java.util.Collections;
import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;


public class Atom implements Expression {
    private final String pattern;
    
    public static final Atom EMPTY = new Atom();
    
    public static Atom of(String pattern) {
        return new Atom(pattern);
    }
    
    public Atom(String pattern) {
        this.pattern = pattern;
    }
    
    public Atom() {
        this("");
    }

    @Override
    public boolean isEmpty() {
        return pattern.isEmpty();
    }

    @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.atom(pattern);
    }
    
    @Override
    public Expression intersect(Expression other) {
        for(int i = 0; i < pattern.length(); ++i) {
            String tail = pattern.substring(i);
            if(startsWith(other, tail)) {
                return of(tail);
            }
        }
        return EMPTY;
    }
    
    private boolean startsWith(Expression where, String what) {
        if(what.length() > Collections.max(where.lengthOptions())) {
            return false;
        }
        
        for(int i = 0; i < what.length(); ++i) {
            if(!where.matchAt(i, what.charAt(i)).equals(
                    Match.MATCH)) {
                return false;
            }
        }
        return true;
    }
    
    public Match matchAt(int i, char c) {
        if(i < pattern.length()) {
            return pattern.charAt(i) == c ? Match.MATCH: Match.NO_MATCH;
        } else {
            return Match.OUT_OF_RANGE;
        }
    }
    
    public String toString() {
        return pattern;
    }

    @Override
    public Set<Integer> lengthOptions() {
        return Collections.singleton(pattern.length());
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
        
        if(obj instanceof Atom) {
            Atom other = (Atom)obj;
            return pattern.equals(other.pattern);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
