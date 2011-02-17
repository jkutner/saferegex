package org.saferegex.parser;

public class EscapeCharacterScanner {
    public static char unescapeCurrent(Source source) {
        source.consumeNextChar();
        if (source.isEmpty()) {
            source.error("Premature end of expression after \\");
        }
    
        try {
            char c = source.consumeNextChar();
            int n = 0;
            if (c == 'x') {
                return decodeANSI(source);
            } else if ((n = "nrtaefv".indexOf(c)) != -1) {
                return "\n\r\t\u0007\u001B\u000C\u000B"
                        .charAt(n);
            } else if(Character.isLetter(c)){
                source.error("Unsupported escape sequence \\" + c);
                return c;
            } else {
                return c;
            }
        } catch (NumberFormatException e) {
            source.error("Malformed ANSI code: "
                    + e.getMessage(), e);
            return 0;
        }
    }

    private static char decodeANSI(Source source) {
        if (source.length() >= 2) {
            String code = source.consumeUntil(2);
            return (char) Integer.parseInt(code, 16);
        } else {
            throw new NumberFormatException(
                    "Malformed ANSI code: "
                            + source.consumeAll());
        }
    }
}
