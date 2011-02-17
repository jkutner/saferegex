package org.saferegex.samples;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.saferegex.parser.CharacterSet;

public class CharacterSamples implements Samples {
    private char[] characters;
    private int size = 0;

    public CharacterSamples(CharacterSet characters, boolean inclusive) {
        if(characters.isEmpty()) {
            this.characters = new char[0];
        } else if(inclusive) {
            this.characters = new char[characters.size()];
            for(char c: characters) {
                add(c);
            }
        } else {
            this.characters = new char[128 - characters.size()];
            addSamplesNotIn(characters, ' ', 'z');
            addSamplesNotIn(characters, 'z', (char)128);
            addSamplesNotIn(characters, (char)0, (char)' ');
        }
    }
    
    private void addSamplesNotIn(CharacterSet set, char start, char end) {
        for(char c = start; c < end; ++c) {
            if(!set.contains(c)) {
                add(c);
            }
        }
    }
    
    public CharacterSamples(int size) {
        characters = new char[size];
    }
    
    public CharacterSamples(String s) {
        characters = new char[s.length()];
        for(int i = 0; i < s.length(); ++i) {
            add(s.charAt(i));
        }
    }

    private void add(char c) {
        if(size == characters.length) {
            throw new NoSuchElementException();
        } 
        characters[size++] = c;
    }
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<String> iterator() {
        return new CharacterSamplesIterator(characters, size());
    }
    
    @Override
    public String toString() {
        return SampleFormatter.format(this);
    }

    private static class CharacterSamplesIterator implements Iterator<String> {
        private final char characters[];
        private final int size;
        private int i = 0;
        
        public CharacterSamplesIterator(char[] characters, int size) {
            this.characters = characters;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return i < size;
        }

        @Override
        public String next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return String.valueOf(characters[i++]);
        }

        @Override
        public void remove() {
            throw new IllegalStateException();
        }
    }
}
