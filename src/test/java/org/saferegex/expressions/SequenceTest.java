package org.saferegex.expressions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.expressions.Expression.Match;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.SamplingStrategy;

public class SequenceTest {
    SamplingStrategy strategy = new ExhaustiveFixedRepetition();
    
    Sequence s = Sequence.of(Atom.of("abc"), 
        Option.of(Atom.of("d"), Atom.of("ef")));
        
    @Test
    public void sequenceToString() {
        assertEquals("abc(d|ef)", s.toString());
    }

    @Test
    public void combinationSamples() {
        /*
        assertEquals("[abcef, abcd]", s.samples(strategy).toString());
        */
        Sequence s2 = Sequence.of(Atom.of("abc"), 
            CharacterClass.including("abcdefg"));
        assertEquals("[abca, abcg, abcf, abcc, abcb, abce, abcd]",
            s2.samples(strategy).toString());
        Sequence s3 = Sequence.of(Atom.of("abc"), 
            Option.of(Atom.of("cde"), Atom.of("cdef")));
        assertEquals("[abccdef, abccde]", s3.samples(strategy).toString());
    }

    @Test
    public void maxLengthOptions() {
        assertEquals("[4, 5]", s.lengthOptions().toString());
    }
    
    @Test
    public void matchAt() {
      assertEquals(Match.MATCH, s.matchAt(0, 'a'));  
      assertEquals(Match.MATCH, s.matchAt(1, 'b'));
      assertEquals(Match.MATCH, s.matchAt(2, 'c'));
      assertEquals(Match.MATCH, s.matchAt(3, 'd'));
      assertEquals(Match.MATCH, s.matchAt(3, 'e'));
      assertEquals(Match.MATCH, s.matchAt(4, 'f'));
      
      assertEquals(Match.NO_MATCH, s.matchAt(0, 'b'));
      assertEquals(Match.NO_MATCH, s.matchAt(3, 'g'));
      assertEquals(Match.NO_MATCH, s.matchAt(4, 'e'));
      assertEquals(Match.OUT_OF_RANGE, s.matchAt(5, 'a'));
    }
    
    @Test
    public void intersection() {
        assertEquals("abc", s.intersect(Atom.of("abc")).toString());
        assertEquals("bc", s.intersect(Atom.of("bcd")).toString());
    }
    
    @Test
    public void equals() {
        Sequence s2 = Sequence.of(Atom.of("abc"), 
            Option.of(Atom.of("d"), Atom.of("ef")));
        assertEquals(s, s2);
        Sequence s3 = Sequence.of(Atom.of("abd"), 
            Option.of(Atom.of("d"), Atom.of("ef")));
        assertFalse(s.equals(s3));
    }
    
    @Test
    public void sequenceHashCode() {
        Sequence s2 = Sequence.of(Atom.of("abc"), 
            Option.of(Atom.of("d"), Atom.of("ef")));
        assertEquals(s.hashCode(), s2.hashCode());
        Sequence s3 = Sequence.of(Atom.of("abd"), 
            Option.of(Atom.of("d"), Atom.of("ef")));
        assertFalse(s.hashCode() == s3.hashCode());
    }

    @Test
    public void simplify() {
        Sequence s2 = Sequence.of(Atom.EMPTY, 
            Option.of(Atom.of("d"), Atom.of("ef")));
        assertEquals(Option.of(Atom.of("d"), Atom.of("ef")), 
            s2.simplify());
        Sequence s3 = Sequence.of( 
            Option.of(Atom.of("d"), Atom.of("ef")),
            Atom.EMPTY);
        assertEquals(Option.of(Atom.of("d"), Atom.of("ef")), 
            s3.simplify());
        Sequence s4 = Sequence.of(Atom.of("d"), Atom.of("ef"));
        assertEquals(Atom.of("def"), s4.simplify());
        assertTrue(s4.equals(Atom.of("def")));
        assertTrue(Atom.of("def").equals(s4));
        assertTrue(s4.hashCode() == Atom.of("def").hashCode());
    }
}
