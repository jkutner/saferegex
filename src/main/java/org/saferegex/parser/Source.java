package org.saferegex.parser;

import java.util.Stack;
import java.util.regex.PatternSyntaxException;

public class Source {
    private String originalCode;
    private String code;
    private int offset;
    private Stack<String> codeStack = new Stack<String>();
    private Stack<Integer> offsetStack = new Stack<Integer>();
    
    public Source(String code, int offset) {
        this.code = code;
        this.offset = offset;
        this.originalCode = code;
    }
    
    public Source(String code) {
        this(code, 0);
    }

    public boolean startWith(String prefix) {
        return code.startsWith(prefix);
    }
    
    public boolean startWith(char c) {
        return code.charAt(0) == c;
    }
    
    public char charAt(int index) {
        return code.charAt(index);
    }
    
    public char consumeNextChar() {
        char c = code.charAt(0);
        this.code = code.substring(1);
        ++offset;
        return c;
    }
    
    public String consumeUntil(int beginIndex) {
        String consumed = code.substring(0, beginIndex);
        this.code = code.substring(beginIndex);
        offset += beginIndex;
        return consumed;
    }
    
    public void consumeToken(String token) {
        if(length() == 0) {
            return;
        }
        
        String consumed = consumeUntil(token.length());
        if(!token.equals(consumed)) {
            throw new IllegalStateException("Attempt to consume " + token +
                " but consumed " + consumed);
        }
    }
    
    public String consumeUntilToken(String token) {
        String consumed = consumeUntil(
            indexOfOrEnd(token));
        consumeToken(token);
        return consumed;
    }
    
    public int indexOf(char c) {
        return code.indexOf(c);
    }
    
    private int indexOfOrEnd(String s) {
        int index = code.indexOf(s);
        return index > 0 ? index : code.length();
    }
    
    public boolean equals(Object o) {
        if(o instanceof String) {
            return code.equals(o);
        } else {
            return false;
        }
    }
    
    public int length() {
        return code.length();
    }
    
    public boolean isEmpty() {
        return length() == 0;
    }
    
    public String consumeAll() {
        String rest = code;
        offset += code.length();
        code = "";
        return rest;
    }
    
    public String toString() {
        return code;
    }
    
    public void error(String description){
        throw new PatternSyntaxException(description, originalCode, offset);
    }
    
    public void error(String description, Throwable t) {
        PatternSyntaxException e = 
            new PatternSyntaxException(description, originalCode, offset);
        e.initCause(t);
        throw e;
    }
    
    public void push() {
        codeStack.push(code);
        offsetStack.push(offset);
    }
    
    public void pop() {
        code = codeStack.pop();
        offset = offsetStack.pop();
    }    
}
