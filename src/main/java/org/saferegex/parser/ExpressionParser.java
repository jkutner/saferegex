package org.saferegex.parser;

import org.saferegex.expressions.Atom;
import org.saferegex.expressions.CharacterClass;
import org.saferegex.expressions.Expression;
import org.saferegex.expressions.Option;
import org.saferegex.expressions.Optional;
import org.saferegex.expressions.Repetition;

public class ExpressionParser {
    public static Expression parse(String s)  {
        return parse(new Source(s), 0);
    }

    public static Expression parse(Source source) {
        return parse(source, 0);
    }
    
    private static Expression parse(Source source, int depth) {
        StringBuilder result = new StringBuilder();
        Expressions expressions = new Expressions();
        
        while (!source.isEmpty()) {
            char c = source.charAt(0);
            if(c == '\\') {
                backspace(expressions, source, result);
            } else if("^$".indexOf(c) != -1) {
                source.consumeNextChar();
                // Ignore
            } else if(c == '.') {
                source.consumeToken(".");
                append(expressions, result, 
                    CharacterClass.excluding("\r\n"));
            } else if(c == '[') {
                append(expressions, result, 
                    CharacterClassParser.parse(source));
            } else if(c == '?') {
                optional(expressions, source, result);
            } else if("*+{".indexOf(c) != -1) {
                repetition(expressions, source, result);
            } else if(c == '|') {
                option(expressions, 
                    source, result, depth);
            } else if(c == '(') {
                group(expressions, source,
                    result, depth);
            } else if(c == ')') {
                source.consumeToken(")");
                if(depth == 0) {
                    source.error("Missing opening parantheses");
                }
                break;
            } else {
                source.consumeNextChar();
                result.append(c);
            }
        }
        expressions.append(result);
        expressions.flush();
        return expressions.getRoot();
    }

    private static void append(Expressions expressions,
            StringBuilder result, CharacterClass charClass) {
        expressions.append(result);
        expressions.append(charClass);
    }

    private static void repetition(Expressions expressions,
            Source source, StringBuilder result) {
        Expression operand;             
        if(result.length() == 0) {
            operand = expressions.getCurrent();
            if(!operand.isEmpty()) {
                Repetition repetition = RepetitionParser.parseRepetition(source,
                    operand);
                expressions.replaceCurrent(repetition);
            } else {
                operand = expressions.getRoot();
                Repetition repetition = RepetitionParser.parseRepetition(source,
                    operand);
                expressions.replaceRoot(repetition);
            }
        } else {
            operand = getLastCharaterAsExpression(
                expressions, result);
            Repetition repetition = RepetitionParser.parseRepetition(source,
                operand);
            expressions.append(repetition);    
        }
    }

    private static Expression getLastCharaterAsExpression(
            Expressions expressions, StringBuilder result) {
        Expression operand;
        int lastIndex = result.length() - 1;
        char lastChar = result.charAt(lastIndex);
        result.setLength(lastIndex);
        operand = Atom.of(String.valueOf(lastChar));
        expressions.append(Atom.of(result.toString()));
        result.setLength(0);
        return operand;
    }

    private static void optional(Expressions expressions,
            Source source, StringBuilder result) {
        if(!source.isEmpty() && source.startWith('?')) {
            source.consumeToken("?");
        }
        Expression operand;             
        if(result.length() == 0) {
            operand = expressions.getCurrent();
            expressions.replaceCurrent(Optional.term(operand));
            operand = expressions.getCurrent();
            if(!operand.isEmpty()) {
                expressions.replaceCurrent(Optional.term(operand));
            } else {
                operand = expressions.getRoot();
                expressions.replaceCurrent(Optional.term(operand));
            }
        } else {
            operand = getLastCharaterAsExpression(
                expressions, result);
            expressions.append(Optional.term(operand));    
        }
    }


    private static void backspace(Expressions expressions, 
            Source source, StringBuilder result) {
        if (source.length() < 2) {
            source.error("Premature end of expression after \\");
        }
        final char c = source.charAt(1);
        if("dwsDWS".indexOf(c) != -1) {  
            CharacterClass charClass = 
                CharacterClassParser.predefinedCharacterClass(source);
            append(expressions, result, charClass);
        } else if("bB".indexOf(c) != -1) {   
            // TODO: Implement
            source.consumeNextChar();
            source.error("Word boundarys are not implemented yet!");
        } else if(c >= '1' && c <= '9') {   
            // TODO: Implement
            source.consumeNextChar();
            source.error("Backreferences (\\1 through \\9) " +
            		"are not supported yet!");
        } else {
            result.append(
                EscapeCharacterScanner.unescapeCurrent(source));
        }
    }

    private static void group(Expressions expressions,
            Source source, StringBuilder result, int depth) {
        source.consumeToken("(");
        if(!source.isEmpty() && source.charAt(0) == '?') {
            source.error("Advanced syntax (expressions starting with '(?') " +
            		"is not yet supported");
        }
        if(result.length() >= 0) {
            expressions.append(result);
        }        
        expressions.append(parse(source, depth + 1));
    }

    private static void option(Expressions expressions,
            Source source, StringBuilder result, int depth) {
        source.consumeToken("|");
        if(result.length() >= 0) {
            expressions.append(result);
        }
        expressions.flush();
        Expression operand = expressions.getRoot();
        expressions.replaceRoot(Option.of(operand, 
            parse(source, depth)));
    }
}
