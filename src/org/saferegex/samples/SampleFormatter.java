package org.saferegex.samples;

public class SampleFormatter {
    public static String format(Samples samples) {
        StringBuilder sb = new StringBuilder("[");
        int  i = 0;
        for(String sample: samples) {
            if(i > 100) {
                sb.append(",...");
                break;
            } else if(i > 0) {
                sb.append(", ");
            } 
            sb.append(sample);
            ++i;
        }
      
        sb.append("]");
        return sb.toString();
    }
}
