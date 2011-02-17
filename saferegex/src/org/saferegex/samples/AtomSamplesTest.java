package org.saferegex.samples;


import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class AtomSamplesTest {
    AtomSamples s = new AtomSamples("foo");
    
   @Test
   public void iterate() {
       Iterator<String> it = s.iterator();
       assertTrue(it.hasNext());
       assertEquals("foo", it.next());
       assertFalse(it.hasNext());
   }
   
   @Test(expected=NoSuchElementException.class)
   public void iterateFail() {
       Iterator<String> it = s.iterator();
       assertEquals("foo", it.next());
       it.next();
   }

}
