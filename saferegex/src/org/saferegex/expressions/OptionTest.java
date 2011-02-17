package org.saferegex.expressions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.expressions.Expression.Match;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.SamplingStrategy;

public class OptionTest {
    SamplingStrategy strategy = new ExhaustiveFixedRepetition();
    Option option = Option.of(Atom.of("d"), Atom.of("ef"));
        
    @Test
    public void optionToString() {
        assertEquals("(d|ef)", option.toString());
    }
    
    @Test
    public void optionSample() {
        assertEquals("[ef, d]", option.samples(strategy).toString());
    }

    @Test
    public void maxLengthOptions() {
        assertEquals("[1, 2]", 
            option.lengthOptions().toString());
    }

    @Test
    public void matchAt() {
      assertEquals(Match.MATCH, option.matchAt(0, 'd'));
      assertEquals(Match.MATCH, option.matchAt(0, 'e'));
      assertEquals(Match.MATCH, option.matchAt(1, 'f'));
      assertEquals(Match.NO_MATCH, option.matchAt(0, 'f'));
      assertEquals(Match.NO_MATCH, option.matchAt(1, 'g'));
      assertEquals(Match.OUT_OF_RANGE, option.matchAt(2, 'f'));
    }
    
    @Test
    public void intersection() {
        assertEquals("d", option.intersect(Atom.of("d")).toString());
        assertEquals("f", option.intersect(Atom.of("fg")).toString());
        assertEquals("ef", option.intersect(Atom.of("efg")).toString());
        assertTrue(option.intersect(Atom.of("eg")).isEmpty());
    }

    @Test
    public void equals() {
        Option option2 = Option.of(Atom.of("d"), Atom.of("ef"));
        assertEquals(option, option2);
        Option option3 = Option.of(Atom.of("d"), Atom.of("eg"));
        assertFalse(option.equals(option3));
    }
    @Test
    public void optionHashCode() {
        Option option2 = Option.of(Atom.of("d"), Atom.of("ef"));
        assertEquals(option.hashCode(), option2.hashCode());
        Option option3 = Option.of(Atom.of("d"), Atom.of("eg"));
        assertFalse(option.hashCode() == option3.hashCode());
    }

    @Test
    public void simplify() {
        assertEquals(option, option.simplify());
        Option option2 = Option.of(Atom.EMPTY, Atom.of("ef"));
        assertEquals(Atom.of("ef"), option2.simplify());
        Option option3 = Option.of(Atom.of("ef"), Atom.EMPTY);
        assertEquals(Atom.of("ef"), option3.simplify());
        Option option4 = Option.of(Atom.of("ef"), Atom.of("ef"));
        assertEquals(Atom.of("ef"), option4.simplify());
        assertTrue(option4.equals(Atom.of("ef")));
        assertTrue(Atom.of("ef").equals(option4));
        assertTrue(option4.hashCode() == Atom.of("ef").hashCode());
    }
}
