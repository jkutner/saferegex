package org.saferegex.samples;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OptionSamples implements Samples {
    private final Samples a;
    private final Samples b;
    
    public OptionSamples(Samples a, Samples b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean isEmpty() {
        return a.isEmpty() && b.isEmpty();
    }

    @Override
    public int size() {
        return a.size() == Integer.MAX_VALUE ? a.size() :
               b.size() == Integer.MAX_VALUE ? b.size() :
               a.size() + b.size();
    }

    @Override
    public Iterator<String> iterator() {
        return new OptionIterator(a, b);
    }
    
    @Override
    public String toString() {
        return SampleFormatter.format(this);
    }

    private static class OptionIterator implements Iterator<String> {
        private Iterator<String> aSamples;
        private Iterator<String> bSamples;
        
        public OptionIterator(Samples a, Samples b) {
            aSamples = a.iterator();
            bSamples = b.iterator();
        }

        @Override
        public boolean hasNext() {
            return aSamples.hasNext() || bSamples.hasNext();
        }

        @Override
        public String next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            
            if(aSamples.hasNext()) {
                return aSamples.next();
            } else {
                return bSamples.next();
            }
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
