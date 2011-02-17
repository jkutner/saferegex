package org.apache.regexp;

import junit.framework.TestCase;

public class ReDoSTest extends TestCase {
    public void testValidRegexp() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 100; ++i) {
            sb.append('a');
        }
        sb.append('!');
        match("a+", sb.toString());
    }
    
    public void testReDos1() {
        try {
            assertFalse(match("(a+)+b", 
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!"));
        } catch(IllegalStateException e) {
            assertEquals("Calculation timed out!", e.getMessage());
        }
    }
    
    public void testReDos2() {
        try {
            assertFalse(match("a*[ab]*O", 
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!"));
        } catch(IllegalStateException e) {
            assertEquals("Calculation timed out!", e.getMessage());
        }
    }
    
    public boolean match(String pattern, String input) {
        REProgram program = new RECompiler().compile(pattern);
        RE re = new RE(program);
        return re.match(input);
    }
}
