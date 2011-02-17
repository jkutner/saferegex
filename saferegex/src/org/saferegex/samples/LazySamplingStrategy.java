package org.saferegex.samples;

import org.saferegex.parser.CharacterSet;

public class LazySamplingStrategy implements SamplingStrategy {
    private final int minRepetition;
    
    public LazySamplingStrategy(int minRepetition) {
        this.minRepetition = minRepetition;
    }
    
    public LazySamplingStrategy() {
        this(30);
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
        return new OptionalSamples(samples);
    }
    
    @Override
    public Samples option(Samples a, Samples b) {
        return new OptionSamples(a, b);
    }

    @Override
    public Samples repetition(Samples elementSamples,
            Samples substitutionSamples, int start, int end) {
        return new RepetitionSamples(elementSamples, 
            end - start > minRepetition ? 
                    Math.max(start, minRepetition) : start , end);
    }

    @Override
    public Samples sequence(Samples head, Samples tail) {
        return new SequenceSamples(head, tail);
    }
}
