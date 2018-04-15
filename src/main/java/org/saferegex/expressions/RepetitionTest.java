package org.saferegex.expressions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.expressions.Expression.Match;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.ExhaustiveVaryingRepetition;
import org.saferegex.samples.SamplingStrategy;

public class RepetitionTest {
    SamplingStrategy fixed = new ExhaustiveFixedRepetition();
    SamplingStrategy varying = new ExhaustiveVaryingRepetition();
    
    @Test
    public void recursiveSubstitution() {
        assertEquals("aaa", 
            RecursiveRepetition.of(Atom.of("a"), 3, 3).substitution().toString());
        assertEquals("((a|(aa|aaa)))?", 
            RecursiveRepetition.of(Atom.of("a"), 0, 3).substitution().toString());
        assertEquals("(a|(aa|(aaa|aaaa)))", 
            RecursiveRepetition.of(Atom.of("a"), 1, 4).substitution().toString());
        assertEquals("a(a|(aa|aaa))", 
            RecursiveRepetition.of(Atom.of("a"), 2, 4).substitution().toString());
    }
    
    @Test
    public void iterativeSubstitution() {
        assertEquals("aaa", 
            IterativeRepetition.of(Atom.of("a"), 3, 3).substitution().toString());
        assertEquals("(a)?(a)?(a)?", 
            IterativeRepetition.of(Atom.of("a"), 0, 3).substitution().toString());
        assertEquals("a(a)?(a)?(a)?", 
            IterativeRepetition.of(Atom.of("a"), 1, 4).substitution().toString());
        assertEquals("aa(a)?(a)?", 
            IterativeRepetition.of(Atom.of("a"), 2, 4).substitution().toString());
    }
    
    @Test
    public void repetitionToString() {
        assertEquals("(a){3,5}", RecursiveRepetition.of(Atom.of("a"), 3, 5).toString());
    }

    @Test
    public void fixedSamples() {
        assertEquals("[aaaaa]", 
            RecursiveRepetition.of(Atom.of("a"), 5).samples(fixed).toString());
        assertEquals("[aaaaa]", 
            RecursiveRepetition.of(Atom.of("a"), 3, 5).samples(fixed).toString());
        assertEquals("[aaaaa]", 
            IterativeRepetition.of(Atom.of("a"), 5, 5).samples(fixed).toString());
    }
    
    @Test
    public void substitutionSamples() {
        RecursiveRepetition r = RecursiveRepetition.of(Atom.of("a"), 0, 5);
        assertEquals("[, aa, aaa, aaaaa, a, aaaa]", 
            r.substitution().samples(fixed).toString());
        assertEquals("[, aa, aaa, aaaaa, a, aaaa]", 
            r.samples(varying).toString());
        assertEquals("[aaaaa, aaa, aaaa]", 
            RecursiveRepetition.of(Atom.of("a"), 3, 5).samples(varying).toString());
        assertEquals("[aaaaa]", 
            IterativeRepetition.of(Atom.of("a"), 5, 5).samples(varying).toString());
    }
    
    @Test
    public void maxLengthOptions() {
        assertEquals("[3]", 
            RecursiveRepetition.of(Atom.of("a"), 3, 3).lengthOptions().toString());
        assertEquals("[0, 1, 2, 3, 4]", 
            RecursiveRepetition.of(Atom.of("a"), 0, 4).lengthOptions().toString());
        assertEquals("[2, 3, 4]", 
            RecursiveRepetition.of(Atom.of("a"), 2, 4).lengthOptions().toString());
    }
    
    @Test
    public void matchAt() {
      Repetition exactlyThree = RecursiveRepetition.of(Atom.of("a"), 3, 3);
      assertEquals(Match.MATCH, exactlyThree.matchAt(0, 'a'));
      assertEquals(Match.MATCH, exactlyThree.matchAt(1, 'a'));
      assertEquals(Match.MATCH, exactlyThree.matchAt(2, 'a'));
      assertEquals(Match.OUT_OF_RANGE, exactlyThree.matchAt(3, 'a'));
      assertEquals(Match.NO_MATCH, exactlyThree.matchAt(2, 'e'));
      
      Repetition twoToFour = RecursiveRepetition.of(Atom.of("a"), 2, 3);
      assertEquals(Match.MATCH, twoToFour.matchAt(0, 'a'));
      assertEquals(Match.MATCH, twoToFour.matchAt(1, 'a'));
      assertEquals(Match.MATCH, twoToFour.matchAt(2, 'a'));
      assertEquals(Match.OUT_OF_RANGE, exactlyThree.matchAt(3, 'a'));
      assertEquals(Match.NO_MATCH, exactlyThree.matchAt(2, 'e'));
    }
    
    @Test
    public void intersect() {
        Repetition a = RecursiveRepetition.of(Atom.of("a"), 10);
        Repetition aab = RecursiveRepetition.of(Atom.of("aab"), 10);
        assertEquals("aa", a.intersect(aab).toString());
        Repetition bc = IterativeRepetition.of(Atom.of("bc"), 10);
        Repetition bcd = IterativeRepetition.of(Atom.of("bcd"), 10);
        assertEquals("bc", bc.intersect(bcd).toString());
        
        Repetition a$ = IterativeRepetition.of(a, 10);
        assertEquals("aaaaaaaaaa", 
            a$.intersect(a).toString());   
    }
}
