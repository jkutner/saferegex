package org.saferegex.parser;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.saferegex.expressions.Expression;

public class CharacterClassTest extends
        ExpressionParserTest {
    @Test
    public void singleCharacterClass() {
        assertMatchWithRE("a", "[a]");
    }
    
    @Test
    public void characterClass() {
        assertMatchWithRE("[bca]", "[abc]");
    }
    
    @Test
    public void nonCharacterClass() {
        assertMatchWithRE("[^bca]", "[^abc]");
    }
    
    @Test
    public void characterClassWithEscapeCharacter() {
        assertMatchWithRE("[\\\tba]", "[\tab\\\\]");
    }
    
    @Test
    public void characterClassWithRange() {
        assertMatchWithRE("[fgdebcanolmjkhiwvutsrqpzyx]", 
            "[a-z]");
    }
    
    @Test
    public void mixedCharacterClassWithRange() {
        assertMatchWithRE("[^dbca\t]", 
            "[^a-d\\tcd]");
    }
    
    @Test
    public void mixedCharacterClassWithUnion() {
        assertMatchWithRE("[$3210765498DEFGABCLMNOHIJKUTWVQPSR_YXZfgdebcanolmjkhiwvutsrqpzyx]", 
            "[$\\w]");
    }
    
    @Test
    public void atomBeforeCharacterClass() {
        assertMatchWithRE("abca", "abc[a]");
        assertMatchWithRE("abc[fgdeh]", "abc[d-h]");
    }
    
    @Test
    public void atomAfterCharacterClass() {
        assertMatchWithRE("[ba]cde", "[ab]cde");
    }
    
    @Test
    public void dotCharacterClass() {
        assertMatchWithRE("a[^\r\n]", "a.");
    }
    
    @Test
    public void digitCharacterClass() {
        assertMatchWithRE("[3210765498]", "\\d");
        assertMatchWithRE("[^3210765498]", "\\D");
    }
    
    @Test
    public void wordCharacterClass() {
        assertMatchWithRE("[3210765498DEFGABCLMNOHIJKUTWVQPSR_YXZfgdebcanolmjkhiwvutsrqpzyx]", "\\w");
        assertMatchWithRE("[^3210765498DEFGABCLMNOHIJKUTWVQPSR_YXZfgdebcanolmjkhiwvutsrqpzyx]", "\\W");
    }
    
    @Test
    public void whiteSpaceCharacterClass() {
        String regexp = "\\s";
        Expression expr = ExpressionParser.parse(new Source(regexp));
        String expressionString = expr.toString();
        assertTrue(expressionString.startsWith("["));
        assertMatchAnyCharacter(expr, regexp, "\t\r\n\f ");
    }
    
    @Test
    public void noWhiteSpaceCharacterClass() {
        String regexp = "\\S";
        Expression expr = ExpressionParser.parse(new Source(regexp));
        String expressionString = expr.toString();
        assertTrue(expressionString.startsWith("[^"));
        assertMatchAnyCharacter(expr, regexp, "\t\r\n\f ");
    }
    
    @Test
    public void wixedPredefinedCharacterClass() {
        assertMatchWithRE("a[3210765498]c", "a\\dc");
    }
    
    @Test
    public void characterClassesWithRepetition() {
        assertMatchWithRE("([DEFGABCLMNOHIJKUTWVQPSRYXZ-]){1,}@" +
        		"([DEFGABCLMNOHIJKUTWVQPSRYXZ-]){1,}." +
        		"([DEFGABCLMNOHIJKUTWVQPSRYXZ]){2,4}", 
        		"[A-Z-]+@[A-Z-]+\\.[A-Z]{2,4}");
    }
}
