package murlen.util.fscript;

import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * <b>LineLoader - used by FScript to load source text</b>
 * <p>
 * <I>Copyright (C) 2000 murlen.</I></p>
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.</p>
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA </p>
 * @author murlen
 * @author Joachim Van der Auwera
 * @version 1.12
 *
 * changes by Joachim Van der Auwera
 * 20.08.2001
 *   - getLine added
 *   - setCurLine test was wrong, allowed setting line one too far
 * 02.08.2002
 *   - allow commands to be split over multiple line by using the "..." continuation mark
 * 07.10.2002
 *   - do not join lines when the ... is at the end of a comment
 * 08.11.2002 but do join them when the hash is inside a quote (and does not indicate a comment)
 * 17.09.2003 moved checking for mismatched quotes and brackets here
 * 24.11.2003 (jvda) use char[] instead of String to store lines
 */

final class LineLoader {

    ArrayList lines;
    private String contLine;    // previous line if it ended with "..." (but without the "...")
    private char[] emptyLine=new char[0];

    int curLine;
    String forError;

    /**
     *Constructor */
    LineLoader() {

        lines=new ArrayList(200);
        curLine=0;
    }

    /**
     * load with script from InputStreamReader
     * @param is  - the input stream to read from
     */
    final void load(Reader is) throws IOException {

        BufferedReader in = new BufferedReader(is);
        String s;
        s=in.readLine();

        while(s!=null) {
            addLine(s);
            s=in.readLine();
        }

        in.close();

        curLine=0;
    }

    /**
     * resets the LineLoader  */
    final void reset() {
        lines=new ArrayList(200);
        curLine=0;
    }

    /**
     * method to incrementally add lines to buffer
     * @param s the line to load */
    final void addLine(String s) {
        if (!s.trim().equals("")) {
            if (s.endsWith("...") && !hasComment(s)) {
                if (contLine!=null) {
                    contLine+=s.substring(0,s.length()-3);
                } else {
                    contLine=s.substring(0,s.length()-3);
                }
            } else {
                if (contLine!=null) {
                    contLine+=s;
                } else {
                    contLine=s;
                }
                char line[]=contLine.toCharArray();
                checkLine(line);
                lines.add(line);
                contLine=null;
            }
        } else {
            //need to add blank lines to keep error msg lines
            //in sync with file lines.
            lines.add(emptyLine);
        }
    }

    private boolean hasComment(String s) {
        boolean cont;
        int hash, quote;
        while (!s.equals("")) {
            hash=s.indexOf('#');
            if (hash==-1) return false;
            quote=s.indexOf('\"');
            if (quote==-1 || hash<quote) return true;
            // hash may be inside a string, so remove quoted part
            cont=true;
            while (cont) {
                s=s.substring(quote+1);
                quote=s.indexOf('\"');
                if (quote==0 || s.charAt(quote-1)!='\\') cont=false;
            }
            s=s.substring(quote+1);
        }
        return false;
    }

    /**
     * Sets the current execution line
     * @param n the line number */
    final void setCurLine(int n) {
        if (n>lines.size()) {
            n=lines.size()-1;
        } else if (n<0) {
            n=0;
        }

        curLine=n;
    }

    /**
     * Returns the current execution line */
    final int getCurLine() {
        return curLine;
    }

    /**
     * Returns the total number of lines in buffer */
    final int lineCount() {
        return lines.size();
    }

    /**
     * Returns the text of the current line */
    final char[] getLine() {
        return (char[])lines.get(curLine);
    }

    /**
     * Returns the text of the current line as a String */
    final String getLineAsString() {
        if ( curLine == -1 ) return forError;
        return new String((char[])lines.get(curLine));
    }

    /**
     *Returns the text of the requested line*/
    final char[] getLine(int n){
        if (n<0 || n>=lines.size()) return emptyLine;
        return (char[])lines.get(n);
    }

   /**
     * Returns the text of the requested line as a String */
    final String getLineAsString(int n) {
        if (n<0 || n>=lines.size()) return "";
        return new String((char[])lines.get(n));
    }

    /**
     * Checks line for correctly formed ( ) and "
     */
    static void checkLine(char chars[]) {
        boolean inQuotes=false;
        int brCount=0;
        int n;

        if (chars!=null) {
            for (n=0 ; n<chars.length ; n++) {
                if (chars[n]=='#' && !inQuotes) {
                    n=chars.length;
                } else {
                    if (inQuotes) {
                        if (chars[n]=='"') {
                            if (n>=1) {
                                if (chars[n-1]!='\\') {
                                    inQuotes=false;
                                }
                            }
                        }
                    } else {
                        if (chars[n]=='(') {
                            brCount++;
                        } else if (chars[n]==')') {
                            brCount--;
                        } else if (chars[n]=='"') {
                            if (n>=1) {
                                if (chars[n-1]!='\\') {
                                    inQuotes=true;
                                }
                            }
                        }
                    }
                }
            }


            if (inQuotes) {
                throw new RuntimeException("Mismatched quotes\n"+new String (chars));
            }
            if (brCount!=0) {
                throw new RuntimeException("Mismatched brackets\n"+new String (chars));
            }
        }

    }


}
