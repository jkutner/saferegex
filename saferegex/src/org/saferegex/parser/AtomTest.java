package org.saferegex.parser;

import org.junit.Test;

public class AtomTest extends ExpressionParserTest {
    @Test
    public void atom() {
        assertMatchWithRE("abc", "abc");
    }
        
    @Test
    public void atomWithTrailingBackslash() {
        assertInvlidSyntax("Premature end of expression after \\", 
            "abc\\");
    }
    
    @Test
    public void atomWithBackslashShouldIgnoreBackslash() {
        assertMatchWithRE("abc(", "abc\\(");
    }
    
    @Test
    public void atomWithANSIMetacharacter() {
        assertMatchWithRE("abc\u00A9def", "abc\\xA9def");
    }
    
    @Test
    public void atomWithMalformedANSIMetacharacter() {
        assertInvlidSyntax("Malformed ANSI code: For input string: \"ZZ\"", 
            "abc\\xZZdef");
    }
    
    @Test
    public void atomWithTruncatedANSIMetacharacter() {
        assertInvlidSyntax("Malformed ANSI code: Malformed ANSI code: 0", "abc\\x0");
    }
    
    @Test
    public void atomWithKnownMetacharacters() {
        assertMatchWithRE("abc\n\r\t\fdef", "abc\\n\\r\\t\\fdef");
    }

}
