package org.saferegex.samples;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.saferegex.parser.CharacterSet;

public class LazySamplingStrategyTest {

    LazySamplingStrategy strategy = 
        new LazySamplingStrategy();
    
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void atom() {
        assertEquals("[abc]", strategy.atom("abc").toString());
    }
    
    @Test
    public void characterClass() {
        assertEquals("[b, c, a]", 
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
        assertEquals("[abc, ]", 
            strategy.optional(new AtomSamples("abc")).toString());
        SampleSet s = new SampleHashSet(2, new AtomSamples("a"), new AtomSamples("b"));
        assertEquals("[b, a, ]", 
            strategy.optional(s).toString());
    }
    
    @Test
    public void option() {
        Samples a = new AtomSamples("abc");
        Samples b = new AtomSamples("def");
        Samples ab = strategy.option(a, b);
        assertEquals("[abc, def]", ab.toString());
        Samples c = new AtomSamples("ghi");
        assertEquals("[abc, def, ghi]", 
            strategy.option(ab, c).toString());
    }
    
    @Test
    public void sequence() {
        assertEquals("[ab]", strategy.sequence(
            new AtomSamples("a"), new AtomSamples("b")).toString());
        Samples ab = strategy.option(new AtomSamples("a"), new AtomSamples("b"));
        assertEquals("[ac, bc]", strategy.sequence(
            ab, new AtomSamples("c")).toString());
        Samples abc = strategy.option(ab, new AtomSamples("c"));
        assertEquals("[ad, bd, cd]", strategy.sequence(
            abc, new AtomSamples("d")).toString());
    }
    
    @Test
    public void repetition() {
        AtomSamples a = new AtomSamples("a");
        assertEquals("[, a, aa, aaa, aaaa, aaaaa]", strategy.repetition(a, null, 0, 5).toString());
        assertEquals("[aaaaa]", strategy.repetition(a, null, 5, 5).toString());
        assertEquals("[aaa, aaaa, aaaaa, aaaaaa]", 
            strategy.repetition(a, null, 3, 6).toString());
        Samples s = strategy.option(new AtomSamples("a"), new AtomSamples("b"));
        assertEquals("[aaa, bbb, aaaa, bbbb, aaaaa, bbbbb]", 
            strategy.repetition(s, null, 3, 5).toString());
        assertEquals("[, a, b, aa, bb, aaa, bbb]", 
            strategy.repetition(s, null, 0, 3).toString());
        assertEquals("[, a, aa, aaa, aa, aaaa, aaaaaa, aaa, aaaaaa, aaaaaaaaa]", 
            strategy.repetition(strategy.repetition(a, null, 1, 3), 
                null, 0, 3).toString());
        assertEquals("[, a, b, c, aa, bb, cc]", 
            strategy.repetition(strategy.repetition(
                new CharacterSamples("abc"), null, 1, 2), 
                null, 0, 1).toString());
    }
}
