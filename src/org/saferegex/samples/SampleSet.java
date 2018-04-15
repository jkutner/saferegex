package org.saferegex.samples;

import java.util.Set;

public interface SampleSet extends Samples, Set<String> {
    public void addAll(Samples samples);
    public boolean isFull();
}