package org.saferegex.expressions;

import static org.junit.Assert.*;
import org.junit.Test;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.SamplingStrategy;


public class OptionalTest {
    SamplingStrategy strategy = new ExhaustiveFixedRepetition();
    
    @Test
    public void optionalSequenceSample() {
        assertEquals("[d, ]",
            Optional.term(Atom.of("d")).samples(strategy).toString());
        Sequence s = Sequence.of(Atom.of("abc"), 
            Optional.term(Atom.of("d")));
        assertEquals("[abcd, abc]", s.samples(strategy).toString());
    }
}
