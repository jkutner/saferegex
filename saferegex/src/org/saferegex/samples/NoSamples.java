package org.saferegex.samples;

import java.util.Collections;
import java.util.Iterator;

public class NoSamples implements Samples {
    public static NoSamples INSTANCE = new NoSamples();
    
    private NoSamples() {}

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<String> iterator() {
        return Collections.EMPTY_SET.iterator();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int size() {
        return 0;
    }
}
