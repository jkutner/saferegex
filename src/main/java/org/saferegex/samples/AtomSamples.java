package org.saferegex.samples;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AtomSamples implements Samples {
    private final String element;
    
    public AtomSamples(String element) {
        this.element = element;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Iterator<String> iterator() {
        return new SingleElementIterator(element);
    }

    @Override
    public String toString() {
        return "[" + element + "]";
    }
    
    private static class SingleElementIterator implements Iterator<String> {
        private String element;
        
        public SingleElementIterator(String element) {
            this.element = element;
        }

        @Override
        public boolean hasNext() {
            return element != null;
        }

        @Override
        public String next() {
            if(!hasNext())
                throw new NoSuchElementException();
            String e = element;
            element = null;
            return e;
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
