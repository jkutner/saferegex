package org.saferegex.expressions;

import java.util.Collections;
import java.util.Set;

import org.saferegex.parser.CharacterSet;
import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class CharacterClass implements Expression {
    private final CharacterSet set;
    private final boolean including;
    
    public static CharacterClass including(CharacterSet set) {
        return new CharacterClass(set, true);
    }
    
    public static CharacterClass excluding(CharacterSet set) {
        return new CharacterClass(set, false);
    }
    
    public static CharacterClass including(String s) {
        return new CharacterClass(CharacterSet.of(s), true);
    }

    public static CharacterClass excluding(String s) {
        return new CharacterClass(CharacterSet.of(s), false);
    }

    
    public CharacterClass(CharacterSet set,
            boolean inclusive) {
        this.set = set;
        this.including = inclusive;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if(!including) {
            sb.append("^");
        }
        for(char c: set) {
            sb.append(c);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Expression intersect(Expression other) {
        Expression p = Atom.EMPTY; 
        for(char c: set) {
            Match match = other.matchAt(0, c);
            if((including && match.equals(Match.MATCH)) ||
               (!including && match.equals(Match.NO_MATCH))) {
                p = Option.of(Atom.of(String.valueOf(c)), p);
            } else if(match.equals(Match.OUT_OF_RANGE)) {
                return Atom.EMPTY;
            }
        }
        return p.simplify();
    }

    @Override
    public boolean isEmpty() {
        return including && set.isEmpty();
    }

    @Override
    public Match matchAt(int i, char c) {
        if(i > 0) {
            return Match.OUT_OF_RANGE;
        } else {
            return including == set.contains(c)
                ? Match.MATCH : Match.NO_MATCH;
        }
    }
    
    @Override
    public Set<Integer> lengthOptions() {
        return Collections.singleton(1);
    }

   @Override
    public Samples samples(SamplingStrategy strategy) {
        return strategy.characterClass(set, including);
    }
    
    @Override
    public Expression simplify() {
        if(isEmpty()) {
            return Atom.EMPTY;
        } if(set.size() == 1 && including) {
            return Atom.of(String.valueOf(
                set.iterator().next()));
        } else {
            return this;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            obj = ((Expression)obj).simplify();
        }
    
        if(obj instanceof CharacterClass) {
            CharacterClass other = (CharacterClass)obj;
            return including == other.including && 
                set.equals(other.set);
        } else {
            Expression self = simplify();
            if(self instanceof CharacterClass) {
                return false;
            } else {
                return self.equals(obj);
            }
        }
    }

    @Override
    public int hashCode() {
        Expression self = simplify();
        if(self instanceof CharacterClass) {
            final int prime = 31;
            int result = 1;
            result = prime * result + set.hashCode();
            result = prime * result + (including ? 1 : 0);
            return result;
        } else {
            return self.hashCode();
        }
    }
}
