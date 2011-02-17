package org.saferegex.expressions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.saferegex.expressions.Expression.Match;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.SampleHashSet;
import org.saferegex.samples.SampleSet;
import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;


public class CharacterClassTest {
   SamplingStrategy strategy = new ExhaustiveFixedRepetition();
    
   @Test
   public void emptyClassIsEmptyPattern() {
       CharacterClass empty = 
           CharacterClass.including("");
       assertTrue(empty.isEmpty());
       CharacterClass all = 
           CharacterClass.excluding("");
       assertFalse(all.isEmpty());
   }

   @Test
   public void shouldSampleCharacter() {
       assertEquals("[b, c, a]", CharacterClass.including("abc")
           .samples(strategy).toString());
       Samples samples = CharacterClass.excluding("abc")
           .samples(strategy);
       assertEquals(128 - 3, samples.size());
       SampleSet set = new SampleHashSet(samples.size(), samples);
       assertFalse(set.contains("a"));
       assertFalse(set.contains("b"));
       assertFalse(set.contains("c"));
   }

   @Test
   public void shouldMatchCharacterAt() {
       CharacterClass abc = CharacterClass.including("abc");
       assertEquals(Match.MATCH, abc.matchAt(0, 'a'));
       assertEquals(Match.MATCH, abc.matchAt(0, 'b'));
       assertEquals(Match.MATCH, abc.matchAt(0, 'c'));
       assertEquals(Match.NO_MATCH, abc.matchAt(0, 'd'));
       assertEquals(Match.OUT_OF_RANGE, abc.matchAt(1, 'x'));
       
       CharacterClass nabc = CharacterClass.excluding("abc");
       assertEquals(Match.NO_MATCH, nabc.matchAt(0, 'a'));
       assertEquals(Match.NO_MATCH, nabc.matchAt(0, 'b'));
       assertEquals(Match.NO_MATCH, nabc.matchAt(0, 'c'));
       assertEquals(Match.MATCH, nabc.matchAt(0, 'd'));
       assertEquals(Match.OUT_OF_RANGE, nabc.matchAt(1, 'x'));
   }

   @Test
   public void maxLengthOptions() {
       assertEquals(1, CharacterClass.including("abc")
           .lengthOptions().toArray()[0]);
   }

   @Test
   public void intersection() {
       CharacterClass a = CharacterClass.including("a");
       assertEquals("a", a.intersect(Atom.of("a")).toString());
       assertEquals("", a.intersect(Atom.of("b")).toString());
       CharacterClass na = CharacterClass.excluding("a");
       assertEquals("", na.intersect(Atom.of("a")).toString());
       assertEquals("a", na.intersect(Atom.of("b")).toString());
   }

   @Test
   public void testToString() {
       assertEquals("[dbca]", 
           CharacterClass.including("abcd").toString());
       assertEquals("[^dbca]", 
           CharacterClass.excluding("abcd").toString());
   }
   
   @Test
   public void equals() {
       assertEquals(Atom.EMPTY, CharacterClass.including(""));
       assertEquals(CharacterClass.including("abc"),
           CharacterClass.including("abc"));
       assertFalse(CharacterClass.including("abc").equals(
           CharacterClass.including("cde")));
       assertEquals(Atom.of("a"), 
           CharacterClass.including("a"));
   }

   @Test
   public void characterClassHashCode() {
       assertEquals(Atom.EMPTY.hashCode(), 
           CharacterClass.including("").hashCode());
       assertEquals(CharacterClass.including("abc").hashCode(),
           CharacterClass.including("abc").hashCode());
       assertFalse(CharacterClass.including("abc").hashCode() ==
           CharacterClass.including("cde").hashCode());
   }

   @Test
   public void simplify() {
       assertEquals(Atom.of("a"), CharacterClass.including("a"));
       assertFalse(Atom.of("a").equals(CharacterClass.excluding("a")));
   }
}
