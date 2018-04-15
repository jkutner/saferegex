package org.saferegex.samples;

import org.saferegex.parser.CharacterSet;

public interface SamplingStrategy {
   public Samples atom(String pattern);
   public Samples characterClass(CharacterSet set, boolean inclusive);
   public Samples optional(Samples samples);
   public Samples option(Samples a, Samples b);
   public Samples sequence(Samples head, Samples tail);
   public Samples repetition(Samples elementSamples, 
           Samples substitutionSamples, int start, int end);
}
