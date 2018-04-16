package org.saferegex.samples;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.parser.CharacterSet;

public class ExhaustiveFixedRepetitionTest {

    ExhaustiveFixedRepetition strategy = 
        new ExhaustiveFixedRepetition(5, 6, 3);
    
    @Test
    public void atom() {
        assertEquals("[abc]", strategy.atom("abc").toString());
    }
    
    @Test
    public void characterClass() {
        assertEquals("[a, b, c]",
            strategy.characterClass(CharacterSet.of("abc"), 
                true).toString());
        SampleSet s = new SampleHashSet(128, 
            strategy.characterClass(CharacterSet.of("abc"), 
                false));
        assertFalse(s.contains("a"));
        assertFalse(s.contains("b"));
        assertFalse(s.contains("c"));
        assertTrue(s.contains("A"));
        assertTrue(s.contains("d"));
        assertEquals(128 - 3, s.size());
    }
    
    @Test
    public void optional() {
        assertEquals("[, abc]", 
            strategy.optional(new AtomSamples("abc")).toString());
        SampleSet s = new SampleHashSet(2, new AtomSamples("a"), new AtomSamples("b"));
        assertEquals("[, a, b]",
            strategy.optional(s).toString());
    }
    
    @Test
    public void option() {
        Samples a = new AtomSamples("abc");
        Samples b = new AtomSamples("def");
        assertEquals("[def, abc]", strategy.option(a, b).toString());
    }
    
    @Test
    public void sequence() {
        Samples a = new AtomSamples("abc");
        Samples b = new AtomSamples("de");
        assertEquals("[abcde]", strategy.sequence(a, b).toString());
        SampleSet s = new SampleHashSet(3, a, b, new AtomSamples("f"));
        assertEquals("[fghij, deghij]", strategy.sequence(
            s, new AtomSamples("ghij")).toString());
    }
    
    @Test
    public void repetition() {
        Samples a = new AtomSamples("a");
        assertEquals("[aaaaa]", strategy.repetition(a, null, 3, 5).toString());
        assertEquals("[aaaaa]", strategy.repetition(a, null, 3, 6).toString());
        SampleSet s = new SampleHashSet(2, new AtomSamples("a"), new AtomSamples("b"));
        assertEquals("[aaaaa, bbbbb]", 
            strategy.repetition(s, null, 3, 5).toString());
    }
}
