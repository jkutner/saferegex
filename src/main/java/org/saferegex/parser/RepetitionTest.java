package org.saferegex.parser;

import org.junit.Test;

public class RepetitionTest extends
        ExpressionParserTest {
    @Test
    public void repetitionNoneOrMany() {
        assertMatchWithRE("(a){0,}", "a*");
    }

    @Test
    public void repetitionOneOrMany() {
        assertMatchWithRE("(a){1,}", "a+");
    }
    
    @Test
    public void fixedRepetition() {
        assertMatchWithRE("(a){1}", "a{1}");
        assertMatchWithRE("(a){5}", "a{5,5}");
    }

    @Test
    public void repetitionBeginningWith() {
        assertMatchWithRE("(a){5,}", "a{5,}");
    }
    
    @Test
    public void limitedRepetition() {
        assertMatchWithRE("(a){5,7}", "a{5,7}");
    }
}
