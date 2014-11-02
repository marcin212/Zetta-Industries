package murlen.util.fscript;
//The lexer - kind of - it started life as a re-implementation of
//StreamTokenizer - hence the peculiarities.

import java.io.*;
import java.util.Hashtable;

/**
 * <b>Re-Implementation of StreamTokenizer for FScript</b>
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
 *
 * <p>This class is a re-implementation of Sun's StreamTokenizer class
 * as it was causing problems (especially parsing -ve numbers).</p>
 * @author murlen
 * @author Joachim Van der Auwera
 * @version 1.12
 *
 * changes by Joachim Van der Auwera
 * 31.08.2001
 *     - simplified (speeded up) handling of comments (there was also an
 *       inconsistency in the newline handling inside and outside comments).
 *     - small mistake disallowed the letter 'A' in TT_WORD
 *
 * declares a string a, which is initialised to one double quote.
 * 11.8.2002 (murlen) Changed to allow \n \r \" \t in strings
 * 17.02.2003 (jvda) Also allow \\ in strings
 * 23.11.2003 (jvda) major rework of nextT() for speed
 *      renumbered TT_xxx constants for faster switch statements
 * 14.04.2004 (murlen) elsif added
 */

final class LexAnn {
    // table with matches between words and their token values
    private static Hashtable wordToken;

    // maximum line length
    private static final int MAX_LINE_LENGTH=1024;

    //general
    public static final int TT_WORD=9000;
    public static final int TT_INTEGER=9001;
    public static final int TT_DOUBLE=9002;
    public static final int TT_EOF=9003; //never set by this class
    public static final int TT_EOL=9004;
    public static final int TT_STRING=9005;
    public static final int TT_FUNC=9006;
    public static final int TT_ARRAY=9007;
    public static final int TT_NULL=9008;

    //keywords
    public static final int TT_IF=9009;
    public static final int TT_EIF=9010;
    public static final int TT_ELSE=9011;
    public static final int TT_THEN=9012;
    public static final int TT_ELSIF=9013;

    public static final int TT_DEFFUNC=9014;
    public static final int TT_EDEFFUNC=9015;
    public static final int TT_WHILE=9016;
    public static final int TT_EWHILE=9017;
    public static final int TT_DEFINT=9018;
    public static final int TT_DEFSTRING=9019;
    public static final int TT_DEFDOUBLE=9020;
    public static final int TT_DEFOBJECT=9021;
    public static final int TT_RETURN=9022;

    //math opts
    public static final int TT_PLUS=9023;
    public static final int TT_MINUS=9024;
    public static final int TT_MULT=9025;
    public static final int TT_DIV=9026;
    public static final int TT_MOD=9027;

    //logic
    public static final int TT_LAND=9028;
    public static final int TT_LOR=9029;
    public static final int TT_LEQ=9030;
    public static final int TT_LNEQ=9031;
    public static final int TT_LGR=9032;
    public static final int TT_LLS=9033;
    public static final int TT_LGRE=9034;
    public static final int TT_LLSE=9035;
    public static final int TT_NOT=9036;

    //other
    public static final int TT_EQ=9037;

    /** contains the current token type */
    int ttype;

    /** Current token value object */
    Object value;

    private boolean pBack;
    private char cBuf[],line[];
    private int length;
    private int c=0;
    private static final int EOL=-1;
    private int pos=0;

    /** String representation of token */
    public String toString(){

        Class c=getClass();
        int n=0;
        String tokenName="",ret="";

        java.lang.reflect.Field[] fields=c.getFields();

        //try to get the human readable TT_* name via reflec magic
        for(n=0;n<fields.length;n++){
            java.lang.reflect.Field f=fields[n];
            try{
                if (f.getName().startsWith("TT")){
                    if (ttype==f.getInt(this)){
                        tokenName=f.getName();
                    }
                }
            }
            catch (Exception e){}

        }

        if (!tokenName.equals("")){
            ret=tokenName + ":" + value;
        }
        else {
            ret=String.valueOf((char)ttype) + ":" + value;
        }

        return ret;
    }


    /**Constructor*/
    LexAnn() {
        //note hard limit on how long a string can be
        cBuf=new char[MAX_LINE_LENGTH];
    }

    /**Convinience constructor which sets line as well*/
    LexAnn(char[] firstLine) {
        this();
        setString(firstLine);
    }

    /**
     * Sets the internal line buffer
     * @param str - the string to use
     */
    void setString(char[] str) {
        length=str.length;
        line=str;
        pos=0;
        c=0;
    }

    /**
     *return the next char in the buffer
     */
    private int getChar() {
        if (pos<length) {
            return line[pos++];
        } else {
            return EOL;
        }
    }

    /**
     * return the character at a current line pos (+offset)
     * without affecting internal counters*/
    private int peekChar(int offset) {
        int n;

        n=pos+offset-1;
        if (n>=length) {
            return EOL;
        } else {
            return line[n];
        }
    }


    /**Read the next token
     * @return int - which is the charater read (not very useful)*/
    int nextToken() throws IOException{

        if (!pBack) {
            return nextT();
        } else {
            pBack=false;
            return ttype;
        }

    }



    /**Causes next call to nextToken to return same value*/
    void pushBack() {
        pBack=true;
    }

    /**
     * get the line which is currently being parsed
     * @return
     */
    String getLine() {
        return new String(line);
    }


    //Internal next token function
    private int nextT() {
        int cPos=0;
        boolean getNext;
        value=null;
        ttype=0;

        while (ttype==0) {
            getNext=true;
            switch (c) {
            // start of line of whitespace
            case 0:
            case ' ': case '\t': case '\n': case '\r':
                break;

            // end of line marker
            case EOL:
                ttype=TT_EOL;
                break;

            // comments
            case '#':
                pos=length;     // skip to end of line
                ttype=TT_EOL;
                break;

            // quoted strings
            case '"':
                c=getChar();
                while ((c!=EOL) && (c!='"')) {
                    if (c=='\\'){
                        switch (peekChar(1)) {
                        case 'n' :
                            cBuf[cPos++]='\n';
                            getChar();
                            break;
                        case 't' :
                            cBuf[cPos++]='\t';
                            getChar();
                            break;
                        case 'r' :
                            cBuf[cPos++]='\r';
                            getChar();
                            break;
                        case '\"' :
                            cBuf[cPos++]='"';
                            getChar();
                            break;
                        case '\\' :
                            cBuf[cPos++]='\\';
                            getChar();
                            break;
                        }
                    } else {
                        cBuf[cPos++]=(char)c;
                    }
                    c=getChar();
                }
                value=new String(cBuf,0,cPos);
                ttype=TT_STRING;
                break;

            // Words
            case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
            case 'G': case 'H': case 'I': case 'J': case 'K': case 'L':
            case 'M': case 'N': case 'O': case 'P': case 'Q': case 'R':
            case 'S': case 'T': case 'U': case 'V': case 'W': case 'X':
            case 'Y': case 'Z': case 'a': case 'b': case 'c': case 'd':
            case 'e': case 'f': case 'g': case 'h': case 'i': case 'j':
            case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
            case 'q': case 'r': case 's': case 't': case 'u': case 'v':
            case 'w': case 'x': case 'y': case 'z':
                while ((c>='A' && c<='Z') || (c>='a' && c<='z')
                        || (c>='0' && c<='9') || c=='_' || c=='.') {
                    cBuf[cPos++]=(char)c;
                    c=getChar();
                }
                getNext=false;
                value=new String(cBuf,0,cPos);
                Integer tt=(Integer)wordToken.get(value);
                if (tt!=null) {
                    ttype=tt.intValue();
                } else {
                    if (c=='(') {
                        ttype=TT_FUNC;
                    } else if (c=='[') {
                        ttype=TT_ARRAY;
                    } else {
                        ttype=TT_WORD;
                    }
                }
                break;

            // Numbers
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                boolean isDouble=false;
                while ((c>='0' && c<='9') || c=='.') {
                    if (c=='.') isDouble=true;
                    cBuf[cPos++]=(char)c;
                    c=getChar();
                }
                getNext=false;
                String str=new String(cBuf,0,cPos);
                if (isDouble) {
                    ttype=TT_DOUBLE;
                    value=new Double(str);
                } else {
                    ttype=TT_INTEGER;
                    value=new Integer(str);
                }
                break;

            // others
            case '+':
                ttype=TT_PLUS;
                break;
            case '-':
                ttype=TT_MINUS;
                break;
            case '*':
                ttype=TT_MULT;
                break;
            case '/':
                ttype=TT_DIV;
                break;
            case '%':
                ttype=TT_MOD;
                break;
            case '>':
                if (peekChar(1)=='=') {
                    getChar();
                    ttype=TT_LGRE;
                } else {
                    ttype=TT_LGR;
                }
                break;
            case '<':
                if (peekChar(1)=='=') {
                    getChar();
                    ttype=TT_LLSE;
                } else {
                    ttype=TT_LLS;
                }
                break;
            case '=':
                if (peekChar(1)=='=') {
                    getChar();
                    ttype=TT_LEQ;
                } else {
                    ttype=TT_EQ;
                }
                break;
            case '!':
                if (peekChar(1)=='=') {
                    getChar();
                    ttype=TT_LNEQ;
                } else {
                    ttype=TT_NOT;
                }
                break;
            default:
                if ((c=='|') &&( peekChar(1)=='|')) {
                    getChar();
                    ttype=TT_LOR;
                } else if ((c=='&') &&( peekChar(1)=='&')) {
                    getChar();
                    ttype=TT_LAND;
                } else {
                    ttype=c;
                }
            }
            if (getNext) c=getChar();
        }
        return ttype;
    }

    static {
        wordToken=new Hashtable();
        wordToken.put("if", new Integer(TT_IF));
        wordToken.put("then", new Integer(TT_THEN));
        wordToken.put("endif", new Integer(TT_EIF));
        wordToken.put("else", new Integer(TT_ELSE));
	wordToken.put("elsif", new Integer(TT_ELSIF));
	wordToken.put("elseif", new Integer(TT_ELSIF));
        wordToken.put("while", new Integer(TT_WHILE));
        wordToken.put("endwhile", new Integer(TT_EWHILE));
        wordToken.put("func", new Integer(TT_DEFFUNC));
        wordToken.put("endfunc", new Integer(TT_EDEFFUNC));
        wordToken.put("return", new Integer(TT_RETURN));
        wordToken.put("int", new Integer(TT_DEFINT));
        wordToken.put("string", new Integer(TT_DEFSTRING));
        wordToken.put("double", new Integer(TT_DEFDOUBLE));
        wordToken.put("object", new Integer(TT_DEFOBJECT));
        wordToken.put("null", new Integer(TT_NULL));
    }
}



