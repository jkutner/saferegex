package org.saferegex.samples;


import static org.junit.Assert.*;

import org.junit.Test;

public class ExhaustiveVaryingRepetitionTest {
    ExhaustiveVaryingRepetition strategy = 
        new ExhaustiveVaryingRepetition(5, 6, 3);
    
    @Test
    public void repetition() {
        Samples a = new AtomSamples("a");
        assertNull(strategy.repetition(a, null, 3, 5));
    }
}
