package org.saferegex.samples;


import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class SampleHashSetTest {
    SampleSet s;
    
    @Before
    public void setUp() throws Exception {
        s = new SampleHashSet(2, 3);
        s.add("a");
        s.add("bc");
    }

    @Test
    public void sampleToString() {
        assertEquals(2, s.size());
        assertEquals("[a, bc]", s.toString());
    }
    
    @Test
    public void testAdd() {
        assertFalse(s.isFull());
        s.add("de");
        assertTrue(s.isFull());
        s.add("ef");
        assertEquals("[a, bc, de]", s.toString());
    }
    
    @Test
    public void testAddAllOfSamples() {
        s.addAll(new AtomSamples("de"));
        assertEquals("[a, bc, de]", s.toString());
        assertTrue(s.isFull());
        s.addAll(new AtomSamples("de"));
        assertEquals("[a, bc, de]", s.toString());
    }
    
    @Test
    public void testAddAllOfCollection() {
        s.addAll(Collections.singleton("de"));
        assertEquals("[a, bc, de]", s.toString());
        assertTrue(s.isFull());
        s.addAll(Collections.singleton("ef"));
        assertEquals("[a, bc, de]", s.toString());
    }
}
