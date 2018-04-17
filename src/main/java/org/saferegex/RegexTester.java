package org.saferegex;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.saferegex.expressions.Atom;
import org.saferegex.expressions.Expression;
import org.saferegex.expressions.NestedExpression;
import org.saferegex.parser.ExpressionParser;
import org.saferegex.samples.LazySamplingStrategy;
import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public class RegexTester {
    private static final int MAX_SAMPLE_SIZE = 10000;
    private static final int MAX_TESTS = 100000;

    public static void main(String[] args) {
        try {
            if(args.length < 1) {
                System.out.println("Usage: java -jar saferegex.jar <regular expression>");
                System.exit(1);
            }
            test(args[0], MAX_SAMPLE_SIZE);
        } catch(PatternSyntaxException e) {
            System.out.println(e.getMessage());
        } catch(Throwable e) {
            System.out.println("A system error occured: " +
                (e.getMessage() != null ? e.getMessage() : e.getClass().toString()));
        }
    }

    public static boolean isVulnerable(String regex) {
        Result result = test(regex, MAX_SAMPLE_SIZE);
        if(result.state().equals(State.VULNERABLE)) {
            return true;
        } else if(result.state.equals(State.DONT_KNOW)) {
            throw new IllegalStateException();
        } else {
            return false;
        }
    }
    enum State {
        VULNERABLE, PROBABLY_NOT_VULNERABLE, NOT_VULNERABLE, DONT_KNOW
    }

    static class Result {       
        private final State state;
        private final int maxSampleLength;
        private final String sample;
        
        private Result(State state, int maxSampleLength, String sample) {
            this.state = state;
            this.maxSampleLength = maxSampleLength;
            this.sample = sample;
        }
        
        private Result(String sample) {
            this(State.VULNERABLE, 0, sample);
        }
        
        private Result(int maxSampleLength) {
            this(State.PROBABLY_NOT_VULNERABLE, 
                maxSampleLength, "");
        }
        
        private Result() {
            this(State.NOT_VULNERABLE, 0, "");
        }

        public State state() {
            return state;
        }

        public int maxSampleLength() {
            return maxSampleLength;
        }
        
        public String toString() {
            if(state.equals(State.VULNERABLE)) {
                return "This expression is vulnerable.\nSample input: " + sample;
            } else if(state.equals(State.NOT_VULNERABLE)) {
                return "This expression is not vulnerable.";
            } else if(state.equals(State.PROBABLY_NOT_VULNERABLE)) {
                return "This expression is probably not vulnerable for sample sizes < " + 
                    maxSampleLength;
            } else {
                return "Cannot tell wether this expression is vulnerable or not.";
            }
        }
    }
    
    public static Result test(String regex, 
            int maxSampleSize) {
        Result result = new Result(State.DONT_KNOW, 0, "");
        try {
            Pattern pattern = Pattern.compile(regex);
            
            System.out.println();
            System.out.println("Testing: " + regex);
            Expression expression = ExpressionParser.parse(regex);
            
            if(!(expression instanceof NestedExpression)) {
                result = new Result();
                return result;
            }
            
            result = test(pattern, expression,
                new LazySamplingStrategy(0), maxSampleSize);  
            return result;
        } catch(FoundVulnerableSampleException e) {
            result = new Result(e.sample);
            return result;
        } finally {
            System.out.println(result.toString());
        }
    }
    
    private static Result test(Pattern pattern,
            Expression expression, SamplingStrategy strategy,
            int maxSampleSize) {
        Samples samples = expression.samples(strategy);
        int samplesTotal = samples.size();
        if(samplesTotal < Integer.MAX_VALUE - 1) {        
            System.out.println(samplesTotal + " samples found.");
        } else {
            System.out.println("More than " + maxSampleSize + " samples found.");
        }
        
        TimeoutExecutor executor = new TimeoutExecutor();
        TimeoutMatcher re = new TimeoutMatcher(executor, pattern);
        
        char postfix = determinePostfix(expression);        
        String sample = "";

        int tests = 0;
        int brokenSamples = 0;
        boolean maxSampleSizeReached = false;
        
        try {
            for(String s: samples) {
                ++tests;
                
                if(tests > MAX_TESTS) {
                    return new Result(State.DONT_KNOW, maxSampleSize, "");
                }
                
                if(s.length() > maxSampleSize) {
                    maxSampleSizeReached = true;
                    break;
                }
                
                if(!re.match(s)) {
                    ++brokenSamples;
                }
                
                if(Math.pow(tests, 2D) % 100D == 0) {
                   System.out.print("*");
                }
                
                sample = s + postfix;
                // System.out.println(sample);
                re.match(sample);
            }

            System.out.println();
            System.out.println("Tests: " + tests);
            System.out.println("Broken samples: " + brokenSamples);
            
            if(tests == 0 || brokenSamples / tests > 90) {
                return new Result(State.DONT_KNOW, maxSampleSize, "");
            } else if(!maxSampleSizeReached) {
                return new Result();
            } else {
                return new Result(maxSampleSize);
            }
        } catch(IllegalStateException e) {
            System.out.println();
            throw new FoundVulnerableSampleException(sample, e);
        } 
    }


    private static char determinePostfix(
            Expression expression) {
        char postfix = (char)0;
        for(char c = 'a'; c < Character.MAX_VALUE; ++c) {
            Expression postfixExpression = 
                Atom.of(String.valueOf(c));
            if(expression.intersect(postfixExpression).isEmpty()) {
                postfix = c;
                break;
            }
        }
        return postfix;
    }
    
    static class FoundVulnerableSampleException extends RuntimeException {
        private static final long serialVersionUID = -3881129052115662750L;
        private String sample;
        
        public FoundVulnerableSampleException(String sample, Throwable t) {
            super(t);
            this.sample = sample;
        }
    }
}
