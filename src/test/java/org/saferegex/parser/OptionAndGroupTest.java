package org.saferegex.parser;

import static org.junit.Assert.*;

import org.apache.regexp.RESyntaxException;
import org.junit.Test;

public class OptionAndGroupTest extends
        ExpressionParserTest {
    @Test
    public void optional() {
        assertMatchWithRE("a(b)?", "ab?");
        assertMatchWithRE("abcde(b)?x", "abcdeb?x");
    }
    
    @Test
    public void option() {
        assertMatchWithRE("(a|b)", "a|b");
        assertMatchWithRE("(a(b)?|(c)?a)", "ab?|c?a");
    }
    
    @Test
    public void noOpeningParentheses() {
        assertInvlidSyntax("Missing opening parantheses", 
            "abc)de");
    }
    
    @Test
    public void group() {
        assertMatchWithRE("a", "(a)");
        assertMatchWithRE("(ab|c)", "(ab|c)");
        assertMatchWithRE("a(bc|de)f", "a(bc|de)f");
    }
    
    @Test
    public void groupWithOption() {
          assertMatchWithRE("a(b)?", "a(b)?");
    }  
    
    @Test
    public void groupWithRepetition() {
        assertMatchWithRE("(a){0,}", "(a)*");
    }
    
    @Test
    public void groupWithOptionAndRepetition() {
        try {
            assertMatchWithRE("((a|(a)?)){1,}", "(a|a?)+");
            fail();
        } catch(RESyntaxException e) {
            assertEquals("Syntax error: Closure operand can't be nullable", e.getMessage());
        };
        assertMatchWithRE("((a|aa)){0,}", "(a|aa)*");
    }
    
    @Test
    public void testNestedGroupsWithOptions() {
        assertMatchWithRE("(a|(b|c))", "(a|(b|c))");
    }

    @Test
    public void testURLRegex() {
        try {
        assertMatchWithRE("(ht|f)tp(s)?://" +
        		"[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]" +
        		"(([-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]){0,}" +
        		"[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]){0,}" +
        		"(:(0-9){0,}){0,}(/)?" +
        		"(([#$%&'+,-./0123456789:;=?ABCDEFGHIJKLMNOPQRSTUVWXYZ\\_abcdefghijklmnopqrstuvwxyz])" +
        		"{0,})?", 
            "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]" +
            "([-.a-zA-Z0-9]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)" +
            "([a-zA-Z0-9\\-\\.\\?\\,\\:\\'\\/\\\\\\+=&amp;%\\$#_]*)?");
        } catch(RESyntaxException e) {}
    }   
}
