package org.saferegex.expressions;

import java.util.Set;

import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;


public interface Expression {
    public boolean isEmpty();
    
    public Match matchAt(int i, char c);
    public Expression intersect(Expression other);
    public Samples samples(SamplingStrategy strategy);
    public Set<Integer> lengthOptions();
    public Expression simplify();
    
    enum Match{MATCH, NO_MATCH, OUT_OF_RANGE};
}
