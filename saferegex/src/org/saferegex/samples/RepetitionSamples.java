package org.saferegex.samples;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RepetitionSamples implements Samples {
    private final Samples samples;
    private final int start;
    private final int end;
    
    public RepetitionSamples(Samples samples, int start, int end) {
        this.samples = samples;
        this.start = start;
        this.end = end;
    }
    
    @Override
    public boolean isEmpty() {
        return start == 0 && end == 0;
    }

    @Override
    public int size() {
        return end - start;
    }

    @Override
    public Iterator<String> iterator() {
        return new RepetitionIterator(samples, start, end);
    }
    
    @Override
    public String toString() {
        return SampleFormatter.format(this);
    }
    
    private static class RepetitionIterator implements Iterator<String> {
        private final Samples samples;
        private final int start;
        private final int end;
        private Iterator<String> elements;
        private int count;

        public RepetitionIterator(Samples samples, 
                int start, int end) {
            this.samples = samples;
            this.elements = samples.iterator();
            this.start = start;
            this.end = end;
            this.count = 0;
        }

        @Override
        public boolean hasNext() {
            return elements.hasNext() && start + count <= end;
        }

        @Override
        public String next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            
            String elementSample = elements.next();
            StringBuilder sample = new StringBuilder(start + count);
            for(int i = 0; i < start + count; ++i) {
                sample.append(elementSample);
            }

            if(!elements.hasNext() || 
                    sample.length() == 0) {
                elements = samples.iterator();
                count = count + 1;
                    //Math.min((count + 1) * 2, end);
            }         
            
            return sample.toString();
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
