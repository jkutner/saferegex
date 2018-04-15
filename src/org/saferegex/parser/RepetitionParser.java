package org.saferegex.parser;

import org.saferegex.expressions.Expression;
import org.saferegex.expressions.IterativeRepetition;
import org.saferegex.expressions.Repetition;

public class RepetitionParser {
    public static Repetition parseRepetition(
            Source source, Expression operand) {
        char c = source.consumeNextChar();
        Repetition repetition = null;
        switch(c) {
        case '+':
            repetition = IterativeRepetition.of(operand, 1, Integer.MAX_VALUE);
            break;
        case '*':
            repetition = IterativeRepetition.of(operand, 0, Integer.MAX_VALUE);
            break;
        case '{':
            repetition = repetitionInBraces(operand,
                    source);
            break;
        default:
            throw new IllegalStateException("Unknown repetition " + c);
        }
        if(!source.isEmpty() && source.startWith('?')) {
            source.consumeToken("?");
        }
        return repetition;
    }

    private static Repetition repetitionInBraces(
            Expression operand, Source source) {
        if(source.length() < 2) {
            source.error("Premature end of repetition '" + 
                source.consumeAll() + "'");
        }
        consumeWhiteSpace(source);
        int from = parseInteger(source);
        checkPositiveInteger(source, from);
        consumeWhiteSpace(source);
        int to = from;
        if(source.charAt(0) == ',') {
            source.consumeToken(",");
            consumeWhiteSpace(source);
            to = parseInteger(source);
        }
        consumeClosingBrace(source);
        
        if(to > 0) {
            return IterativeRepetition.of(operand, from, to);
        } else {
            return IterativeRepetition.of(operand, from, Integer.MAX_VALUE);
        }
    }

    private static void checkPositiveInteger(Source source, int i) {
        if(i == -1) {
            source.error("Error in repetition: Number expected");
        }
    }

    private static void consumeClosingBrace(Source source) {
        consumeWhiteSpace(source);
        if(source.charAt(0) != '}') {
            source.error("Error in repetition: Missing }");
        }
        source.consumeToken("}");
    }

    private static int parseInteger(Source source) {
        StringBuilder number = new StringBuilder();
        while(!source.isEmpty()) {
            if(",} ".indexOf(source.charAt(0)) != -1) {
                break;
            }
            
            char c = source.consumeNextChar();
            if(c >= '0' && c <= '9') {
                number.append(c);
            } else {
                source.error("Error in repetition: Number expected but got " + c);
            }
        }
        
        if(number.length() == 0) {
            return -1;
        }
        
        try {
            return Integer.parseInt(number.toString());
        } catch(NumberFormatException e) {
            source.error("Error in repetition: Invalid number", e);
            return -1;
        }
    }

    private static void consumeWhiteSpace(Source source) {
        while(!source.isEmpty()) {
            if(source.charAt(0) == ' ') {
                source.consumeToken(" ");
            } else {
                break;
            }
        }
    }
}
