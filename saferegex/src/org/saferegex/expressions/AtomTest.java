package org.saferegex.expressions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.expressions.Expression.Match;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.SamplingStrategy;

public class AtomTest {
    SamplingStrategy strategy = new ExhaustiveFixedRepetition();
    
   @Test
   public void emptyStringIsEmptyPattern() {
       assertTrue(Atom.of("").isEmpty());
       assertEquals("", Atom.of("").toString());
   }
   
   @Test
   public void shouldSamplePattern() {
       Atom abc = new Atom("abc");
       assertFalse(abc.isEmpty());
       assertEquals("[abc]", abc.samples(strategy).toString());
   }
   
   @Test
   public void shouldMatchCharacterAt() {
       Atom abcd = new Atom("abcd");
       assertEquals(Match.MATCH, abcd.matchAt(0, 'a'));
       assertEquals(Match.MATCH, abcd.matchAt(1, 'b'));
       assertEquals(Match.MATCH, abcd.matchAt(2, 'c'));
       assertEquals(Match.MATCH, abcd.matchAt(3, 'd'));
       assertEquals(Match.OUT_OF_RANGE, abcd.matchAt(4, 'x'));
   }
   
   @Test
   public void lengthOptions() {
       assertEquals(4, Atom.of("abcd").lengthOptions().toArray()[0]);
   }
   
   @Test
   public void intersection() {
       Atom abcd = new Atom("abcd");
       Atom bcde = new Atom("bcde");
       assertEquals("abcd", abcd.intersect(abcd).toString());
       assertEquals("bcd", abcd.intersect(bcde).toString());
       assertEquals("", abcd.intersect(Atom.of("efg")).toString());
   }
   
   @Test
   public void testToString() {
       assertEquals("abc", Atom.of("abc").toString());
   }
   
   @Test
   public void equals() {
       assertEquals(Atom.of("abc"), Atom.of("abc"));
       assertFalse(Atom.of("abc").equals("abd"));
   }
   
   @Test
   public void atomHashCode() {
       assertEquals(Atom.of("abc").hashCode(), Atom.of("abc").hashCode());
       assertFalse(Atom.of("abc").hashCode() == Atom.of("abd").hashCode());
   }
   
   @Test
   public void simplify() {
       Atom abc = Atom.of("abc");
       assertEquals(abc, abc.simplify());
   }
}
