package org.saferegex;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegexTesterTest {
   @Test
   public void main() {
       RegexTester.main(new String[]{"ab|c"});
   }
    
   @Test
   public void isVulnerable() {
       assertFalse(RegexTester.isVulnerable("abc"));
       assertFalse(RegexTester.isVulnerable("[abc]"));
       assertFalse(RegexTester.isVulnerable("abc|def"));
   }
   
   @Test
   public void nestedRepetitions() {
       assertTrue(RegexTester.isVulnerable("(a|a?)+"));      
       assertTrue(RegexTester.isVulnerable("(a|aa)*"));
       assertTrue(RegexTester.isVulnerable("(a+)+"));
       assertTrue(RegexTester.isVulnerable("([a-zA-Z]+)*"));
       assertTrue(RegexTester.isVulnerable("(.*a){12}"));
   }
    
   @Test
   public void intersectingRepetitions() {
       assertTrue(RegexTester.isVulnerable("a*[ab]*O"));
       assertTrue(RegexTester.isVulnerable("a*[ab]*[ac]*O"));
       assertTrue(RegexTester.isVulnerable("a*b?a*x"));
   }   
   @Test
   public void esapiOld() {
       assertTrue(RegexTester.isVulnerable("(([a-z])+.)+[A-Z]([a-z])+"));
       assertTrue(RegexTester.isVulnerable("^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?[a-zA-Z]*)*$"));
       assertTrue(RegexTester.isVulnerable("([a-zA-Z0-9])(([\\-.]|[_]+)?([a-zA-Z0-9]+))*(@){1}[a-z0-9]+[.]{1}(([a-z]{2,3})|([a-z]{2,3}[.]{1}[a-z]{2,3}))"));
   }
    
   @Test
   public void esapiNew() {
       assertFalse(RegexTester.isVulnerable("^[.a-zA-Z_ ]{0,30}$"));
       assertFalse(RegexTester.isVulnerable(
           "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"));
       assertFalse(RegexTester.isVulnerable("^(\\d{4}[- ]?){3}\\d{4}$"));
       assertFalse(RegexTester.isVulnerable("^([0-6]\\d{2}|7([0-6]\\d|7[012]))([ \\-]?)" +
       		"\\d\\d([ \\-]?)\\d{4}$"));
   }

   @Test
   public void esapiNewURLValidation() {
       assertFalse(RegexTester.isVulnerable(
           "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]" +
            "([-.a-zA-Z_]*[0-9a-zA-Z])*(:(0-9)*)?(\\/?)" +
            "([a-zA-Z0-9\\-\\.\\?\\,\\:\\'\\/\\\\\\+=&amp;%\\$#_]*)?"));
   } 
    
   @Test
   public void regexlib() {
       assertTrue(RegexTester.isVulnerable("[a-z]+@[a-z]+([a-z\\.]+\\.)+[a-z]+"));
       assertTrue(RegexTester.isVulnerable("^([a-z0-9]+([\\-a-z0-9]*[a-z0-9]+)?\\.){0,}" +
            "([a-z0-9]+([\\-a-z0-9]*[a-z0-9]+)?){1,63}(\\.[a-z0-9]{2,7})+$"));
       assertTrue(RegexTester.isVulnerable("^\\d*[0-9](|.\\d*[0-9]|)*$"));
       assertTrue(RegexTester.isVulnerable("^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?" +
       		"[a-zA-Z]*)*\\s+&lt;(A-Za-z_[-._A-Za-z_]*A-Za-z_@A-Za-z_[-._A-Za-z_]*A-Za-z_\\.A-Za-z_{2,3})&gt;$|^" +
       		"(A-Za-z_[-._A-Za-z_]*A-Za-z_@A-Za-z_[-._A-Za-z_]*A-Za-z_\\.A-Za-z_{2,3})$")); 
   }
}
