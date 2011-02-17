package org.saferegex.expressions;

import java.util.HashSet;
import java.util.Set;

public class SequenceSampler {
    public static Set<String> sample(String head, String tail, 
        Set<String> overlappingSample) {
        Set<String> samples = new HashSet<String>();
        for(String body: overlappingSample) {
            String sample = 
                combineIfPossible(head, body, tail);
            if(sample.length() > 1000) {
                // System.out.println("**** Disregading sample of length " + 
                //    sample.length());
                continue;
            }

            if(!sample.isEmpty()) {
                samples.add(sample);
            }
        }
        
        if(samples.isEmpty()) {
            samples.add(head + tail);
        }
        
        return samples;
    }

    protected static String combineIfPossible(String head, String body,
            String tail) {
        StringBuilder sample = new StringBuilder();
        if(body.isEmpty()) {
            // resume
        } else if(2*body.length() == head.length() + tail.length()) {
            addBody(sample, body);
        } else if(head.length() >= body.length() &&
                  tail.length() >= body.length()) {
            addHead(sample, head, body);
            addBody(sample, body);
            addTail(sample, body, tail);
        }
        
        return sample.toString();
    }

    protected static void addBody(StringBuilder sample, String body) {
        sample.append(body);
        sample.append(body);
    }

    protected static void addHead(StringBuilder sample, String head,
            String body) {
        if(head.length() > body.length()) {
            sample.append(head.substring(0, 
                head.length() - body.length()));
        }
    }

    protected static void addTail(StringBuilder sample, String body,
            String tail) {
        if(tail.length() > body.length()) {
            sample.append(
                tail.substring(body.length()));
        }
    }
    
    /*
    public Set<String> overlappingSamples() {
        Set<String> overlappingSamples = head.intersect(tail).samples();
        Set<String> samples = new HashSet<String>();
        for(String headSample: head.samples()) {
            for(String tailSample: tail.samples()) {
                samples.addAll(SequenceSampler.sample(
                    headSample, tailSample, overlappingSamples));
            }
        }
        return samples;
    }    
    
    public Set<String> limitedOverlappingSamples() {
        Set<String> overlappingSamples = head.intersect(tail).samples();
        Set<String> samples = new HashSet<String>();
        int n = 0;
        for(String headSample: head.samples()) {
            for(String tailSample: tail.samples()) {
                samples.addAll(SequenceSampler.sample(
                    headSample, tailSample, overlappingSamples));
                ++n;
                if(n > 100) {
                    return samples;
                }
            }
        }
        return samples;
    } 
    
    public Set<String> combinationSamples() {
        Set<String> samples = new HashSet<String>();
        for(String headSample: head.samples()) {
            for(String tailSample: tail.samples()) {
                samples.add(headSample + tailSample);
            }
        }
        return samples;
    }
    
    Random rand = new Random();
    
    public Set<String> fuzzingSamples() {
        Set<String> samples = new HashSet<String>();
        Set<String> headSamples = head.samples();
        Set<String> tailSamples = tail.samples();
        
        if(headSamples.size() + tailSamples.size() < 100) {
            return combinationSamples();
        }
        
        for(int i = 0; i < 1000; ++i) {
            int m = Math.abs(rand.nextInt() % headSamples.size() - 1); 
            int n = Math.abs(rand.nextInt() % tailSamples.size() - 1); 
            String head = "";
            String tail = "";
            
            Iterator<String> it = headSamples.iterator();
            for(int j = 0; j < m; ++j) {
                head = it.next();
            }

            it = tailSamples.iterator();
            for(int j = 0; j < n; ++j) {
                tail = it.next();
            }
            
            if(head.length() + tail.length() > 1000) {
                continue;
            }
            
            samples.add(head + tail);
            // System.out.println("Guessing: " + (head + tail));
        }
        
        return samples;
    }
    */

}
