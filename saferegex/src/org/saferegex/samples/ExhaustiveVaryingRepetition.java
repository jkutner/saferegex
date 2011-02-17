package org.saferegex.samples;

public class ExhaustiveVaryingRepetition extends
        ExhaustiveFixedRepetition {

    public ExhaustiveVaryingRepetition() {
        super();
    }

    public ExhaustiveVaryingRepetition(
            int maxRepetitionSize, int maxSampleLength,
            int maxNumberOfSamples) {
        super(maxRepetitionSize, maxSampleLength,
                maxNumberOfSamples);
    }

    @Override
    public Samples repetition(Samples elementSamples,
            Samples substitutionSamples, int start, int end) {
        return substitutionSamples;
    }
}
