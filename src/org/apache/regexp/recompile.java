/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.regexp;

import org.apache.regexp.RECompiler;
import org.apache.regexp.RESyntaxException;

/**
 * 'recompile' is a command line tool that pre-compiles one or more regular expressions
 * for use with the regular expression matcher class 'RE'.  For example, the command
 * <code>java org.apache.regexp.recompile re1 "a*b"</code> produces output like this:
 *
 * <pre>
 *
 *    // Pre-compiled regular expression 'a*b'
 *    private static final char[] re1Instructions =
 *    {
 *        0x002a, 0x0000, 0x0007, 0x0041, 0x0001, 0xfffd, 0x0061,
 *        0x0041, 0x0001, 0x0004, 0x0062, 0x0045, 0x0000, 0x0000,
 *    };
 *
 *    private static final REProgram re1 = new REProgram(re1Instructions);
 *
 * </pre>
 *
 * By pasting this output into your code, you can construct a regular expression matcher
 * (RE) object directly from the pre-compiled data (the character array re1), thus avoiding
 * the overhead of compiling the expression at runtime.  For example:
 *
 * <pre>
 *
 *    RE r = new RE(re1);
 *
 * </pre>
 *
 * @see RE
 * @see RECompiler
 *
 * @author <a href="mailto:jonl@muppetlabs.com">Jonathan Locke</a>
 * @version $Id: recompile.java 518156 2007-03-14 14:31:26Z vgritsenko $
 */
public class recompile
{
    /**
     * Main application entrypoint.
     *
     * @param arg Command line arguments
     */
    static public void main(String[] arg)
    {
        // Create a compiler object
        RECompiler r = new RECompiler();

        // Print usage if arguments are incorrect
        if (arg.length <= 0 || arg.length % 2 != 0)
        {
            System.out.println("Usage: recompile <patternname> <pattern>");
            System.exit(0);
        }

        // Loop through arguments, compiling each
        for (int i = 0; i < arg.length; i += 2)
        {
            try
            {
                // Compile regular expression
                String name         = arg[i];
                String pattern      = arg[i+1];
                String instructions = name + "Instructions";

                // Output program as a nice, formatted character array
                System.out.print("\n    // Pre-compiled regular expression '" + pattern + "'\n"
                                 + "    private static final char[] " + instructions + " = \n    {");

                // Compile program for pattern
                REProgram program = r.compile(pattern);

                // Number of columns in output
                int numColumns = 7;

                // Loop through program
                char[] p = program.getInstructions();
                for (int j = 0; j < p.length; j++)
                {
                    // End of column?
                    if ((j % numColumns) == 0)
                    {
                        System.out.print("\n        ");
                    }

                    // Print character as padded hex number
                    String hex = Integer.toHexString(p[j]);
                    while (hex.length() < 4)
                    {
                        hex = "0" + hex;
                    }
                    System.out.print("0x" + hex + ", ");
                }

                // End of program block
                System.out.println("\n    };");
                System.out.println("\n    private static final REProgram " + name + " = new REProgram(" + instructions + ");");
            }
            catch (RESyntaxException e)
            {
                System.out.println("Syntax error in expression \"" + arg[i] + "\": " + e.toString());
            }
            catch (Exception e)
            {
                System.out.println("Unexpected exception: " + e.toString());
            }
            catch (Error e)
            {
                System.out.println("Internal error: " + e.toString());
            }
        }
    }
}
