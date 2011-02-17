package org.saferegex.samples;

import org.saferegex.parser.CharacterSet;

public class ExhaustiveFixedRepetition implements SamplingStrategy {
    private final int maxRepetitionSize;
    private final int maxSampleLength;
    private final int maxNumberOfSamples;
    
    public ExhaustiveFixedRepetition(int maxRepetitionSize,
            int maxSampleLength, int maxNumberOfSamples) {
        this.maxRepetitionSize = maxRepetitionSize;
        this.maxSampleLength = maxSampleLength;
        this.maxNumberOfSamples = maxNumberOfSamples;
    }

    public ExhaustiveFixedRepetition() {
        this(30, 1000, 1000);
    }
    
    @Override
    public Samples atom(String pattern) {
        return new AtomSamples(pattern);
    }

    @Override
    public Samples characterClass(CharacterSet characters,
            boolean inclusive) {
        return new CharacterSamples(characters, inclusive);
    }
    
    @Override
    public Samples optional(Samples samples) {
        SampleSet set = SampleHashSet.of(maxNumberOfSamples, samples);        
        set.add("");
        return set;
    }
    
    @Override
    public Samples option(Samples a, Samples b) {
        return new SampleHashSet(maxNumberOfSamples, a, b);
    }

    @Override
    public Samples repetition(Samples elementSamples,
            Samples substitutionSamples, int start, int end) {
        end = Math.min(end, maxRepetitionSize);
        SampleSet set = new SampleHashSet(
            elementSamples.size() * end, maxNumberOfSamples);
        for(String sample: elementSamples) {
            if(set.isFull()) { break; }
            StringBuilder sb = new StringBuilder(end);
            for(int i = 0; i < end; ++i) {
                sb.append(sample);
            }
            set.add(sb.toString());
        }
        return set;
    }

    @Override
    public Samples sequence(Samples head, Samples tail) {
        SampleSet set = new SampleHashSet(
            head.size() * tail.size(), maxNumberOfSamples);
        for(String headSample: head) {
            if(set.isFull()) { break; }
            for(String tailSample: tail) {
                if(set.isFull()) { break; }
                if(headSample.length() + tailSample.length() 
                        > maxSampleLength) {
                    break;
                }                
                set.add(headSample + tailSample);
            }
        }
        return set;
    }
}
