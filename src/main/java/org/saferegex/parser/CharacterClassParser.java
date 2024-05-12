package org.saferegex.parser;

import org.saferegex.expressions.CharacterClass;

public class CharacterClassParser {

    public static CharacterClass parse(
            Source source) {
        CharacterSet set = new CharacterSet(source.length());
        boolean including = true;
    
        if (source.startWith("[^")) {
            including = false;
            source.consumeToken("[^");
        } else if(source.startWith("[:")) {
            //TODO: Implement
            source.error("Posix character classes are not implemented yet.");
        } else if (source.startWith("[")) {
            including = true;
            source.consumeToken("[");
        } else {
            source.error("Missing [ at the beginning of a character class");
        }
    
        do {
            if (source.isEmpty()) {
                source.error("Missing ']' at the end of character class.");
            }
            if (source.startWith('\\')) {
            	if(source.length() > 1 && "dwsDWS".indexOf(source.charAt(1)) != -1) {
            		CharacterSet subset = 
            			predefinedCharacterClass(source).toSet();
            		set.addAll(including ? subset : subset.inverted());
            	} else {
            		set.add(EscapeCharacterScanner.unescapeCurrent(source));
            	}
            } else if (source.length() >= 3
                    && source.charAt(1) == '-'
                    && source.charAt(2) != ']') {
                set.addAll(parseRange(source));
            } else {
                set.add(source.consumeNextChar());
            }
        } while (!source.startWith("]"));
        source.consumeToken("]");
        return new CharacterClass(set, including);
    }

    private static CharacterSet parseRange(Source source) {
        char from = source.consumeNextChar();
        source.consumeToken("-");
        char to = source.consumeNextChar();
        if(from >= to) {
            source.error("Invalid character range " +
                from + "-" + to);
        }
        CharacterSet range = CharacterSet.of(from, to);
        return range;
    }

    static CharacterClass predefinedCharacterClass(Source source) {
        source.consumeToken("\\");
        
        CharacterSet digit =
            CharacterSet.of('0', '9');
        CharacterSet word = 
            CharacterSet.of('A', 'Z');
        word.addAll(
            CharacterSet.of('a', 'z'));
        word.addAll(
            digit);
        word.add('_');
        CharacterSet whiteSpace = 
            CharacterSet.of(" \t\r\n\f");
        char c = source.consumeNextChar();
        switch(c) {
            case 'd':
                return CharacterClass.including(digit);
            case 'D':
                return CharacterClass.excluding(
                    digit);
            case 'w':
                return CharacterClass.including(
                    word);
            case 'W':
                return CharacterClass.excluding(
                    word);
            case 's':
                return CharacterClass.including(
                    whiteSpace);
            case 'S':
                return CharacterClass.excluding(
                    CharacterSet.of(" \t\r\n\f\u000b\u001c\u001d\u001f\u001e"));
            default:
                throw new IllegalStateException(
                    "Unknown predefined character class \\" + c);
        }
    }
}
