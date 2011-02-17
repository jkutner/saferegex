package org.saferegex.parser;

import org.saferegex.expressions.Atom;
import org.saferegex.expressions.Expression;
import org.saferegex.expressions.Sequence;

public class Expressions {
    private Expression root = Atom.EMPTY;
    private Expression current = Atom.EMPTY;
    
    public Expression getRoot() {
        return root;
    }
    
    public Expression getCurrent() {
        return current;
    }

    public void replaceCurrent(Expression current) {
        this.current = current;
    }
    
    public void replaceRoot(Expression expression) {
        root = expression;
        current = Atom.EMPTY;
    }

    public void append(Expression expression) {
        flush();
        current = expression;
    }
    
    public void append(StringBuilder result) {
        append( Atom.of(result.toString()));
        result.setLength(0);
    }
    
    public void flush() {
        root = Sequence.of(root, current).simplify();
        current = Atom.EMPTY;
    }
}
