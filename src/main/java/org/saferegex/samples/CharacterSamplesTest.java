package org.saferegex.samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class CharacterSamplesTest {
    CharacterSamples s = new CharacterSamples("ab");
    
    @Test
    public void sampleToString() {
        assertEquals(2, s.size());
        assertEquals("[a, b]", s.toString());
    }
    
    @Test
    public void iterate() {
        Iterator<String> it = s.iterator();
        assertTrue(it.hasNext());
        assertEquals("a", it.next());
        assertEquals("b", it.next());
        assertFalse(it.hasNext());
    }
    
    @Test(expected=NoSuchElementException.class)
    public void iterateFail() {
        Iterator<String> it = s.iterator();
        it.next(); it.next(); it.next();
    }    
}
