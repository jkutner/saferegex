package org.saferegex.parser;

import java.util.HashSet;

public class CharacterSet extends HashSet<Character>{
    private static final long serialVersionUID = 1L;

    public CharacterSet(int initialCapacity) {
        super(initialCapacity);
    }

    public static CharacterSet of(String chars) {
        CharacterSet set = new CharacterSet(chars.length());
        for(int i = 0; i < chars.length(); ++i) {
            set.add(chars.charAt(i));
        }
        return set;
    }
    
    public static CharacterSet of(char from, char to) {
        CharacterSet set = new CharacterSet(to - from);
        for(int i = from; i <= to; ++i) {
            set.add((char)i);
        }
        return set;
    }
    
    public CharacterSet inverted() {
    	CharacterSet inverse = new CharacterSet(128 - size());
    	for(int i = 0; i < 128; ++i) {
    		if(!contains((char)i)) {
    			inverse.add((char)i);
    		}
    	}
    	return inverse;
    }
}
