package org.saferegex.parser;

import static org.junit.Assert.assertEquals;
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
        assertMatchWithRE("[abc]", "[abc]");
    }
    
    @Test
    public void nonCharacterClass() {
        assertMatchWithRE("[^abc]", "[^abc]");
    }
    
    @Test
    public void characterClassWithEscapeCharacter() {
        assertMatchWithRE("[\tab\\]", "[\tab\\\\]");
    }
    
    @Test
    public void characterClassWithRange() {
        assertMatchWithRE("[abcdefghijklmnopqrstuvwxyz]",
            "[a-z]");
    }
    
    @Test
    public void mixedCharacterClassWithRange() {
        assertMatchWithRE("[^abcd\t]",
            "[^a-d\\tcd]");
    }
    
    @Test
    public void mixedCharacterClassWithUnion() {
        assertMatchWithRE("[$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz]",
            "[$\\w]");
    }
    
    @Test
    public void atomBeforeCharacterClass() {
        assertMatchWithRE("abca", "abc[a]");
        assertMatchWithRE("abc[hdefg]", "abc[d-h]");
    }
    
    @Test
    public void atomAfterCharacterClass() {
        assertMatchWithRE("[ab]cde", "[ab]cde");
    }
    
    @Test
    public void dotCharacterClass() {
        assertMatchWithRE("a[^\r\n]", "a.");
    }
    
    @Test
    public void digitCharacterClass() {
        assertMatchWithRE("[0123456789]", "\\d");
        assertMatchWithRE("[^0123456789]", "\\D");
    }
    
    @Test
    public void wordCharacterClass() {
        assertMatchWithRE("[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz]", "\\w");
        assertMatchWithRE("[^0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz]", "\\W");
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
        assertMatchWithRE("a[0123456789]c", "a\\dc");
    }
    
    @Test
    public void characterClassesWithRepetition() {
        assertMatchWithRE("([ABCDEFGHIJKLMNOPQRSTUVWXYZ-]){1,}@" +
        		"([ABCDEFGHIJKLMNOPQRSTUVWXYZ-]){1,}." +
        		"([ABCDEFGHIJKLMNOPQRSTUVWXYZ]){2,4}",
        		"[A-Z-]+@[A-Z-]+\\.[A-Z]{2,4}");
    }

    @Test
    public void characterClassWithLiteralRangeSymbol() {
        Expression expr = ExpressionParser.parse(new Source("[a-]"));
        assertEquals("[a-]", expr.toString());
    }
}
