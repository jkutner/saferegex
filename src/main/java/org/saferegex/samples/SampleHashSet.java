package org.saferegex.samples;

import java.util.Collection;
import java.util.HashSet;

public class SampleHashSet extends HashSet<String> implements SampleSet {
    private static final long serialVersionUID = -3245880130748373726L;

    final int maxCapacity;
    
    public static SampleSet of(int maxCapacity, Samples samples) {
        return new SampleHashSet(maxCapacity, samples);
    }
    
    private static int capacity(Samples... sampleSets) {
        int capacity = 0;
        for(Samples samples: sampleSets) {
            capacity += samples.size();
        }
        return capacity + 1;
    }

    public SampleHashSet(int initialCapacity, int maxCapacity) {
        super(Math.min(initialCapacity, maxCapacity));
        this.maxCapacity = maxCapacity;
    }
    
    public SampleHashSet(int maxCapacity, SampleSet set) {
        super(set);
        this.maxCapacity = maxCapacity;
    }

    public SampleHashSet(int maxCapacity, Samples... sampleSets) {
        this(capacity(sampleSets), maxCapacity);
        for(Samples samples: sampleSets) {
            for(String s: samples) {
                add(s);
            }
        }
    }
    
    public void addAll(Samples samples) {
        for(String sample: samples) {
           if(isFull()) {
               break;
           }
           add(sample);
        }
    }

    @Override
    public boolean add(String e) {
        if(!isFull()) {
            return super.add(e);
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        if(c.size() + size() <= maxCapacity) {
            return super.addAll(c);
        } else {
            boolean changed = false;
            for(String sample: c) {
                if(isFull()) {
                    break;
                }
                changed |= add(sample);
            }
            return changed;
        }
    }

    public boolean isFull() {
        return size() >= maxCapacity;
    }
}
