package org.saferegex.parser;

import static org.junit.Assert.*;
import java.util.regex.PatternSyntaxException;

import org.apache.regexp.RE;
import org.apache.regexp.REDebugCompiler;
import org.apache.regexp.REProgram;
import org.saferegex.expressions.Expression;
import org.saferegex.samples.ExhaustiveFixedRepetition;
import org.saferegex.samples.Samples;
import org.saferegex.samples.SamplingStrategy;

public abstract class ExpressionParserTest {
    private REDebugCompiler compiler = new REDebugCompiler();
    
    SamplingStrategy strategy = new ExhaustiveFixedRepetition(
        5, 10000, 10);
    
    public void assertMatchWithRE(String expected, String regexp) {
        Expression expr = parseAndCheck(expected, regexp);
        assertEquals(expected, expr.toString());
        assertMatchAllSamples(expr, regexp);
    }

    public void assertMatchAllSamples(Expression expr,
            String regexp) {
        REProgram program = compiler.compile(regexp);
        RE matcher = new RE(program);
        Samples samples = expr.samples(strategy);
        for(String sample: samples) {
            String s = sample;
            if(s.length() == 1) {
                s = "0x" + Integer.toHexString(s.charAt(0));
            }
            assertTrue(String.format("Sample %s does not match %s", 
                s, regexp), matcher.match(sample));
        }
    }
    
    public void assertInvlidSyntax(String errorMessage, String regexp) {
        try {
            ExpressionParser.parse(new Source(regexp));
            fail("Should throw: " + errorMessage);
        } catch(PatternSyntaxException e) {
            assertTrue(e.getMessage().startsWith(errorMessage));
        }
    }
    
    public Expression parseAndCheck(String expected, String regexp) {
        Expression expr = ExpressionParser.parse(new Source(regexp));
        assertEquals(expected, expr.toString());
        return expr;
    }
    
    public void assertMatchAnyCharacter(Expression expr, 
            String regexp, String characters) {
        String expressionString = expr.toString();
        assertTrue(expressionString.endsWith("]"));
        for(int i = 0; i < characters.length(); ++i) {
            char c = characters.charAt(i);
            assertTrue("Should contain 0x" + 
                Integer.toHexString(c), 
                expressionString.indexOf(c) >= 0);
        }
        assertMatchAllSamples(expr, regexp);
    }
}
