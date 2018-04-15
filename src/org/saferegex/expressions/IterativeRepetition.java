package org.saferegex.expressions;

public class IterativeRepetition extends RecursiveRepetition {
    public static IterativeRepetition of(Expression pattern, int end) {
        return new IterativeRepetition(pattern, end, end);
    }
    
    public static IterativeRepetition of(Expression pattern, int start, int end) {
        return new IterativeRepetition(pattern, start, end);
    }

    public IterativeRepetition(Expression pattern, int start, int end) {
        super(pattern, start, end);
    }    
    
    protected Expression substitute(Expression pattern, int start,
            int end) {
        end = Math.min(end, MAX_REPETITION);
        pattern = pattern.simplify();
        if(end < start) {
            throw new IllegalArgumentException();
        } else if(end == start) {
            return sequence(pattern, start);
        } else {
            return Sequence.of(sequence(pattern, start), 
                    optionalSequence(pattern, end - start)).simplify();
        }
    }
        
    protected static Expression optionalSequence(Expression pattern, int times) {
        Expression p = Atom.EMPTY;
        for(int i = 0; i < times; ++i) {
            p = Sequence.of(p, Optional.term(pattern));
        }
        return p;
    }
}
