package org.saferegex.samples;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SequenceSamples implements Samples {
    private final Samples head;
    private final Samples tail;
    
    public SequenceSamples(Samples head, Samples tail) {
        super();
        this.head = head;
        this.tail = tail;
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty() && tail.isEmpty();
    }

    @Override
    public int size() {
        return head.size() == 0 ? tail.size() : 
            tail.size() == 0 ? head.size() : 
            head.size() == Integer.MAX_VALUE ? head.size() :
            tail.size() == Integer.MAX_VALUE ? tail.size() :
                head.size() * tail.size();
    }

    @Override
    public Iterator<String> iterator() {
        return new SequenceIterator(head, tail);
    }

    @Override
    public String toString() {
        return SampleFormatter.format(this);
    }
    
    private static class SequenceIterator implements Iterator<String> {
        private final Samples head;
        private final Samples tail;
        private Iterator<String> headSamples;
        private Iterator<String> tailSamples;
        
        public SequenceIterator(Samples head, Samples tail) {
            this.head = head;
            this.tail = tail;
            headSamples = head.iterator();
            tailSamples = tail.iterator();
        }

        @Override
        public boolean hasNext() {
            return headSamples.hasNext() || tailSamples.hasNext();
        }

        @Override
        public String next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            
            if(!headSamples.hasNext()) {
                headSamples = head.iterator();
            }
            
            if(!tailSamples.hasNext()) {
                tailSamples = tail.iterator();
            }
            
            StringBuilder sample = new StringBuilder();
            if(headSamples.hasNext()) {
                sample.append(headSamples.next());
            }

            if(tailSamples.hasNext()) {
                sample.append(tailSamples.next());
            }            
            return sample.toString();
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
