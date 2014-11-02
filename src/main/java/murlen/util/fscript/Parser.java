package murlen.util.fscript;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * <b>Parser - Does the parsing - i.e it's the brains of the code.</b>
 * <p>
 * <I>Copyright (C) 2000-2003 murlen.</I></p>
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
 * modifications by Joachim Van der Auwera
 * 14.08.2001 added support for indexed variables
 * 20.08.2001 -clean handling of setVar with null value
 *   - cleaner handling of if with null condition
 *   - make sure running empty script does nothing
 *   - extra info when throwing an exception (with surrounding lines)
 *   - changed operator prioritues for && and ||
 *   - fixed bug in parseIf with handling of nesting of if clauses with else
 *   - check for missing endif or endwhile (caused infinit loops)
 *   - check for a null to prevernt excepion in parseExpr
 * 28.08.2001
 *   - call to host.getVar() replaced by host.F() (and added
 *     proper exception handling, in that case)
 * 31.08.2001
 *   - test on if condition being of correct type re-introduced
 * 10.09.2001
 *   - added < <= > >= on strings
 * 23.11.2001
 *   - allow adding strings with anything
 * 04.12.2001
 *   - allow add/subtract/multiply/divide int with double
 * 24.04.2002
 *   - fixed a bug in parsing of nested if clause when one of the nested items contains a
 *     "then" token with the statements on the next line(s) (test for TT_EOL instead of TT_EOF)
 * 30.04.2002
 *   - handle all exceptions on all getVarEntry, setVarEntry and callFunctionEntry calls
 * 06.05.2002
 *   - error messages now unique
 *   - TT_EOF handled better in some cases
 * 05-06.08.2002
 *   - removed some redundant code
 *   - introduced parseFunctionEnd to catch functions without return (it worked before, so...)
 * 21.10.2002
 *   - added == for FSObject instances
 * 30.10.2002 object now allowed as function parameter
 * 07.11.2002 better error handling when parsing an expression
 * 20.11.2002 make != work for objects
 * 15.03.2003 getContext() added JVDA
 * 17.09.2003 removed checking for mismatched quotes and brackets, and moved to LineLoader
 * 23.11.2003 recycle LexAnn objects (saves object allocation/release) JVDA
 * 24.11.2003 some extra (smallish) speed improvements (also to reduce garbage a little bit)
 * 25.02.2004
 * - proper evaluation of FSObject with contained boolean/integer for if/while
 * - more powerful compare for FSObject instance with something else
 * 14.04.2004 elsif opperator support
 */
final class Parser {
    public static final Integer FS_TRUE=new Integer(1);
    public static final Integer FS_FALSE=new Integer(0);

    //simple data class used internally to store function defs
    class FuncEntry {
        int startLine; //start line of function
        int endLine; //end line of function
        ArrayList paramNames; //list of parameter names
        HashMap params; //hashmap of parameters

        FuncEntry() {
            startLine=0;
            endLine=0;
            paramNames=new ArrayList(4);
            params=new HashMap();
        }

        public String toString() {
            String s;

            s=startLine + " " ;
            s=s+endLine + " ";
            s=s+paramNames + " ";
            s=s+params;

            return s;
        }
    }

    //exception that occurs when someone calls return
    class RetException extends Exception {}

    private LineLoader code; //the code
    private LexAnn tok; //tokenizer

    private int maxLine;
    private HashMap vars; //function local variables
    private HashMap gVars; //global variables
    private static HashMap opPrio; //operator priority table
    private FScript host; //link to hosting FScript object
    private HashMap funcs; //function map
    private Object retVal; //return value

    private Parser subParser;   //nested parser, for callback routines in FSParserExtension

    private String error[];
    /** Public constructor
     * @param h a reference to the FScript object
     */
    Parser(FScript h) {
        vars=new HashMap();
        gVars=null;
        funcs=new HashMap();
        host=h;

        setPrio();
    }

    //only used for function calls - note it is private
    private Parser(FScript h,HashMap l,HashMap g, HashMap f) {
        vars=l;
        gVars=g;
        funcs=f;
        host=h;
    }

    /**
     * Sets the LineLoader clas to be used for input
     * @param in - the class
     */
    void setCode(LineLoader in) {
        code=in;
    }

    /**
     *The main parsing function
     *@param from - the start line number
     *@param to - the end line number
     *returns an Object (currently a Integer or String) depending
     *on the return value of the code parsed, or null if none.
     */
    Object parse(int from,int to) throws IOException, FSException {

        // nothing to do when starting beond the code end
        if (code.lineCount()<=from) return null;


        maxLine=to;
        code.setCurLine(from);
        tok=new LexAnn(code.getLine());
        getNextToken();
        while (tok.ttype!=LexAnn.TT_EOF) {

            //a script must always start with a word...

            try {
                parseStmt();
            } catch (RetException e) {
                return retVal;
            }

            getNextToken();
        }

        return null;


    }




    /**
     * The main parsing function
     * @param line - the line to be parsed
     * @return an Object depending on the return value of the code parsed, or null if none.
     */
    Object parse(String line) throws IOException, FSException {
        int oldLine=code.curLine;
        try {
            code.curLine=-1;
            code.forError=line;
            char[] chars=line.toCharArray();
            LineLoader.checkLine(chars);
            tok=new LexAnn(chars);
            tok.nextToken();
            // a script must always start with a word...
            try {
                parseStmt();
            } catch (RetException e) {
                return retVal;
            }
        } finally {
            code.curLine=oldLine;
        }
        return null;
    }


    /**
     * Resets the parser state.
     */
    void reset(){
        if (vars!=null){
            vars.clear();
        }
        if (gVars!=null){
            gVars.clear();
        }
    }

    //builds the operator priority table
    private void setPrio(){
        if (opPrio==null){
            opPrio=new HashMap();
            //from low to high
            Integer prio;
            prio=new Integer(1);
            opPrio.put(new Integer(LexAnn.TT_LOR),prio);
            prio=new Integer(2);
            opPrio.put(new Integer(LexAnn.TT_LAND),prio);
            prio=new Integer(5);
            opPrio.put(new Integer(LexAnn.TT_LEQ),prio);
            opPrio.put(new Integer(LexAnn.TT_LNEQ),prio);
            opPrio.put(new Integer(LexAnn.TT_LGR),prio);
            opPrio.put(new Integer(LexAnn.TT_LGRE),prio);
            opPrio.put(new Integer(LexAnn.TT_LLS),prio);
            opPrio.put(new Integer(LexAnn.TT_LLSE),prio);
            prio=new Integer(10);
            opPrio.put(new Integer(LexAnn.TT_PLUS),prio);
            opPrio.put(new Integer(LexAnn.TT_MINUS),prio);
            prio=new Integer(20);
            opPrio.put(new Integer(LexAnn.TT_MULT),prio);
            opPrio.put(new Integer(LexAnn.TT_DIV),prio);
            opPrio.put(new Integer(LexAnn.TT_MOD),prio);
        }
    }

    //statement - top level thing
    private void parseStmt() throws IOException, FSException,RetException {


        switch(tok.ttype) {

            case LexAnn.TT_DEFINT:
            case LexAnn.TT_DEFSTRING:
            case LexAnn.TT_DEFDOUBLE:
            case LexAnn.TT_DEFOBJECT: {
                parseVarDef();
                break;
            }

            case LexAnn.TT_IF: {
                parseIf();
                break;
            }
            case LexAnn.TT_WHILE: {
                parseWhile();
                break;
            }
            case LexAnn.TT_RETURN: {
                parseReturn();
                break;
            }
            case LexAnn.TT_DEFFUNC: {
                parseFunctionDef();
                break;
            }
            case LexAnn.TT_EDEFFUNC: {
                parseFunctionEnd();
                break;
            }
            case LexAnn.TT_EIF:
                throw new FSException("unexpected endif");
            case LexAnn.TT_EWHILE:
                throw new FSException("unexpected endwhile");

            case LexAnn.TT_FUNC: {
                parseFunc();
                break;
            }
            case LexAnn.TT_ARRAY: {
                parseArrayAssign();
                break;
            }
            case LexAnn.TT_WORD: {
                parseAssign();
                break;
            }
            case LexAnn.TT_EOL: {
                tok.nextToken();
                break;
            }
            case LexAnn.TT_EOF: {
                // all done
                break;
            }
            default: {
                parseError("Expected identifier "+tok);

            }
        }

    }


    private void parseFunc() throws IOException,FSException {
        String name;

        name=(String)tok.value;

        //should be a '('
        getNextToken();

        parseCallFunc(name);
        getNextToken();
    }

    private void parseArrayAssign() throws IOException,FSException {
        String name;
        Object index;
        Object val;

        name=(String)tok.value;
        getNextToken(); // should be a '['
        getNextToken(); // should be the index
        index=parseExpr();
        getNextToken(); // should be a ']'

        //getNextToken();
        if (tok.ttype!=LexAnn.TT_EQ) {
            parseError("Expected '='" );
        } else {
            getNextToken();
            val=parseExpr();
            try {
                host.setVarEntry(name,index,val);
            } catch (Exception e) {
                parseError(e.getMessage());
            }
        }
    }


    //handles 'return' statements
    private void parseReturn() throws IOException,FSException,RetException {

        getNextToken();

        retVal=parseExpr();
        throw new RetException();
    }

    // handle endif without return, just return 1 (or true)
    private void parseFunctionEnd() throws RetException {
        retVal=FS_TRUE;
        throw new RetException();
    }


    //Asignment parser
    private void parseAssign() throws IOException, FSException {
        String name;
        Object val;

        name=(String)tok.value;
        getNextToken();

        if (tok.ttype!=LexAnn.TT_EQ) {
            parseError("Expected '='" );
        } else {

            getNextToken();
            val=parseExpr();

            if (hasVar(name)) {
                setVar(name,val);
            } else {
                try {
                    host.setVarEntry(name,null,val);
                } catch (Exception e) {
                    parseError(e.getMessage());
                }
            }
        }
    }

    Object callFunction(String name,ArrayList params) throws
    IOException,FSException{

        FuncEntry fDef;
        int n;
        int oldLine;
        Parser oldSubParser;
        Object val;

        val=null;

        //Check we have a definition for the function
        if (funcs.containsKey(name)) {

            fDef=(FuncEntry)funcs.get(name);

            //Check params and def match
            if (fDef.paramNames.size()!=params.size()) {
                parseError("Expected " +
                fDef.paramNames.size() +
                " parameters, Found " + params.size());
            }

            //Create a new parser instance to handle call
            Parser p;
            HashMap locals=new HashMap();

            //Push the params into the local scope
            for (n=0;n<fDef.paramNames.size();n++) {
                locals.put(fDef.paramNames.get(n),
                params.get(n));
            }
            //watch for recursive calls
            if (gVars==null) {
                p=new Parser(host,locals,vars,funcs);
            } else {
                p=new Parser(host,locals,gVars,funcs);
            }
            //cache the current execution point
            oldLine=code.getCurLine();
            p.setCode(code);
            oldSubParser=subParser;
            subParser=p;

            //let it rip
            val=p.parse(fDef.startLine+1,fDef.endLine-1);

            //reset execution point
            subParser=oldSubParser;
            code.setCurLine(oldLine);



        } else {//calls into super class  code...}
            try {
                val=host.callFunctionEntry(name,params);
            } catch (Exception e) {
                parseError(e.getMessage());
            }
        }


        return val;


    }

    //Handle calls to a function
    private Object parseCallFunc(String name) throws
    IOException,FSException {

        ArrayList params=new ArrayList(4);


        //Set up the parameters
        do {
            getNextToken();
            if (tok.ttype==',') {
                getNextToken();
            } else if (tok.ttype==')') {
                break;
            }
            params.add(parseExpr());
        } while (tok.ttype==',');


        return callFunction(name,params);

    }

    //handles function definitions
    private void parseFunctionDef() throws IOException,FSException {

        FuncEntry fDef=new FuncEntry();
        Object val;
        String name,fName;

        fDef.startLine=code.getCurLine();

        getNextToken();

        //should be the function name
        if (tok.ttype!=LexAnn.TT_FUNC) {
            parseError("Expected function start identifier");
        }
        fName=(String)tok.value;
        getNextToken();

        //should be a '('
        if (tok.ttype!='(') {
            parseError("Expected (");
        }

        getNextToken();
        //parse the header...
        while(tok.ttype!=')') {
            val=null; //keep the compiler happy..

            if (tok.ttype==LexAnn.TT_DEFINT) {
                val=FS_FALSE;
            } else if (tok.ttype==LexAnn.TT_DEFSTRING) {
                val=new String("");
            } else if (tok.ttype==LexAnn.TT_DEFOBJECT) {
                val=new FSObject();
            } else {
                parseError("Expected type name");
            }

            getNextToken();

            if (tok.ttype!=LexAnn.TT_WORD) {
                parseError("Expected function parameter name identifier");
            }

            name=(String)tok.value;

            fDef.paramNames.add(name);
            fDef.params.put(name,val);

            getNextToken();
            if (tok.ttype==',') getNextToken();
        }

        //now we just skip to the endfunction

        while ((tok.ttype!=LexAnn.TT_EDEFFUNC)&&(tok.ttype!=LexAnn.TT_EOF)) {
            getNextToken();
            if (tok.ttype==LexAnn.TT_DEFFUNC)
                parseError("Nested functions are illegal");
        }

        fDef.endLine=code.getCurLine();
        getNextToken();

        funcs.put(fName,fDef);

    }


    //Really process tthe expressions (internal recursive calls only), all
    //external calls call parseExpr()
    private Object parseExpr() throws IOException, FSException{

        ETreeNode curNode=null;
        boolean end=false;
        Object val;
        boolean negate=false; //flag for unary minus
        boolean not=false;//flag for unary not.
        boolean prevOp=true;//flag - true if previous value was an operator

        while (!end){

            switch (tok.ttype) {


                //the various possible 'values'
                case LexAnn.TT_INTEGER:
                case LexAnn.TT_DOUBLE:
                case LexAnn.TT_STRING:
                case LexAnn.TT_WORD:
                case LexAnn.TT_FUNC:
                case LexAnn.TT_NULL:
                case LexAnn.TT_ARRAY:{

                    if (!prevOp){
                        parseError("Expected Operator");
                    } else {

                        val=null;
                        ETreeNode node=new ETreeNode();
                        node.type=ETreeNode.E_VAL;

                        switch (tok.ttype){
                            //numbers - just get them
                            case LexAnn.TT_INTEGER:{
                                val=tok.value;
                                break;
                            }
                            case LexAnn.TT_DOUBLE:{
                                val=tok.value;
                                break;
                            }
                            //functions - evaluate them
                            case LexAnn.TT_FUNC:{
                                String name=(String)tok.value;
                                getNextToken();
                                val=parseCallFunc(name);
                                break;
                            }
                            //arrays - evaluate them
                            case LexAnn.TT_ARRAY:{
                                String name=(String)tok.value;
                                getNextToken(); //should be a '['
                                getNextToken(); //should be the index
                                Object index=parseExpr();
                                try {
                                    val=host.getVarEntry(name,index);
                                } catch (Exception e) {
                                    parseError(e.getMessage());
                                }
                                break;
                            }
                            //variables - resolve them
                            case LexAnn.TT_WORD:{
                                if (hasVar((String)tok.value)) {
                                    val=getVar((String)tok.value);
                                } else {
                                    try {
                                        val=host.getVarEntry((String)tok.value,null);
                                    } catch (Exception e) {
                                        parseError(e.getMessage());
                                    }
                                }
                                break;
                            }
                            //strings - just get again
                            case LexAnn.TT_STRING:{
                                val=tok.value;
                                break;
                            }
                            //null
                            case LexAnn.TT_NULL:{
                                val=new FSObject(null);
                                break;
                            }
                        }

                        //unary not
                        if (not){
                            if (val instanceof Integer){
                                if (((Integer)val).intValue()==0){
                                    val=FS_TRUE;
                                } else {
                                    val=FS_FALSE;
                                }
                                not=false;
                            } else if (val instanceof FSObject && ((FSObject)val).getObject() instanceof Boolean) {
                                if (((FSObject)val).getObject().equals(Boolean.FALSE)) {
                                    val=FS_TRUE;
                                } else {
                                    val=FS_FALSE;
                                }
                            } else if (val instanceof FSObject && ((FSObject)val).getObject() instanceof Integer) {
                                if (((Integer)((FSObject)val).getObject()).intValue()==0) {
                                    val=FS_TRUE;
                                } else {
                                    val=FS_FALSE;
                                }
                            } else {
                                String msg=val.getClass().getName();
                                if (val instanceof FSObject) msg="FSObject with "+((FSObject)val).getNullClass().getName();
                                parseError("Type mismatch for ! "+msg);
                            }
                        }

                        //unary minus
                        if (negate) {
                            if (val instanceof Integer){
                                val=new Integer(-((Integer)val).intValue());
                            } else if (val instanceof Double){
                                val=new Double(-((Double)val).doubleValue());
                            } else {
                                parseError("Type mistmatch for unary -");
                            }
                        }

                        node.value=val;

                        if (curNode!=null){
                            if (curNode.left==null){
                                curNode.left=node;
                                node.parent=curNode;
                                curNode=node;

                            } else if (curNode.right==null){
                                curNode.right=node;
                                node.parent=curNode;
                                curNode=node;

                            }
                        } else {
                            curNode=node;
                        }

                        prevOp=false;
                    }
                    break;
                }
                /*operators - have to be more carefull with these.
                We build an expression tree - inserting the nodes at the right
                points to get a reasonable approximation to correct operator
                precidence*/
                case LexAnn.TT_LEQ:
                case LexAnn.TT_LNEQ:
                case LexAnn.TT_MULT:
                case LexAnn.TT_DIV:
                case LexAnn.TT_MOD:
                case LexAnn.TT_PLUS:
                case LexAnn.TT_MINUS:
                case LexAnn.TT_LGR:
                case LexAnn.TT_LGRE:
                case LexAnn.TT_LLSE:
                case LexAnn.TT_LLS:
                case LexAnn.TT_NOT:
                case LexAnn.TT_LAND:
                case LexAnn.TT_LOR: {
                    if (prevOp){
                        if (tok.ttype==LexAnn.TT_MINUS){
                            negate=true;
                        } else if (tok.ttype==LexAnn.TT_NOT){
                            not=true;
                        } else {
                            parseError("Expected Expression");
                        }
                    } else {

                        ETreeNode node=new ETreeNode();

                        node.type=ETreeNode.E_OP;
                        node.value=new Integer(tok.ttype);

                        if (curNode.parent!=null){

                            int curPrio=getPrio(tok.ttype);
                            int parPrio=
                            getPrio(((Integer)curNode.parent.value).intValue());

                            if (curPrio<=parPrio){
                                //this nodes parent is the current nodes grandparent
                                node.parent=curNode.parent.parent;
                                //our nodes left leg is now linked into the current nodes
                                //parent
                                node.left=curNode.parent;
                                //hook into grandparent
                                if (curNode.parent.parent!=null){
                                    curNode.parent.parent.right=node;
                                }

                                //the current nodes parent is now us (because of above)
                                curNode.parent=node;
                                //set the current node.
                                curNode=node;
                            } else {
                                //current node's parent's right is now us.
                                curNode.parent.right=node;
                                //our nodes left is the current node.
                                node.left=curNode;
                                //our nodes parent is the current node's parent.
                                node.parent=curNode.parent;
                                //curent nodes parent is now us.
                                curNode.parent=node;
                                //set the current node.
                                curNode=node;
                            }
                        } else {
                            //our node's left is the current node
                            node.left=curNode;
                            //current node's parent is us now
                            //we don't have to set our parent, as it is null.
                            curNode.parent=node;
                            //set current node
                            curNode=node;
                        }
                        prevOp=true;
                    }
                    break;
                }
                case '(':
                    //start of an bracketed expression, recursively call ourself
                    //to get a value
                {
                    getNextToken();
                    val=parseExpr();

                    if (negate) {
                        if (val instanceof Integer){
                            val=new Integer(-((Integer)val).intValue());
                        } else if (val instanceof Double){
                            val=new Double(-((Double)val).doubleValue());
                        } else {
                            parseError("Type mistmatch for unary -");
                        }
                    }

                    ETreeNode node=new ETreeNode();
                    node.value=val;
                    node.type=ETreeNode.E_VAL;

                    if (curNode!=null){
                        if (curNode.left==null){
                            curNode.left=node;
                            node.parent=curNode;
                            curNode=node;

                        } else if (curNode.right==null){
                            curNode.right=node;
                            node.parent=curNode;
                            curNode=node;

                        }
                    } else {
                        curNode=node;
                    }
                    prevOp=false;
                    break;
                }

                default: {
                    end=true;
                }

            }
            if (!end){
                tok.nextToken();
            }
        }

        //find the top of the tree we just built.
        if (curNode==null) parseError("Missing Expression");
        while(curNode.parent!=null){
            curNode=curNode.parent;
        }


        return evalETree(curNode);

    }

    //convenience function to get operator priority
    private int getPrio(int op){
        return ((Integer)opPrio.get(new Integer(op))).intValue();
    }

    //evaluates the expression tree recursively
    private Object evalETree(ETreeNode node) throws FSException{
        Object lVal,rVal;

        if ( node == null )
        {
            parseError("Malformed expression");
            // this is never reached, just for readability
            return null;
        }

        if (node.type==ETreeNode.E_VAL){
            return node.value;
        }
        lVal=evalETree(node.left);
        rVal=evalETree(node.right);

        switch (((Integer)node.value).intValue()){
            //call the various eval functions
            case LexAnn.TT_PLUS:{
                return evalPlus(lVal,rVal);
            }
            case LexAnn.TT_MINUS:{
                return evalMinus(lVal,rVal);
            }
            case LexAnn.TT_MULT:{
                return evalMult(lVal,rVal);
            }
            case LexAnn.TT_DIV:{
                return evalDiv(lVal,rVal);
            }
            case LexAnn.TT_LEQ:{
                return evalEq(lVal,rVal);
            }
            case LexAnn.TT_LNEQ:{
                return evalNEq(lVal,rVal);
            }
            case LexAnn.TT_LLS:{
                return evalLs(lVal,rVal);
            }
            case LexAnn.TT_LLSE:{
                return evalLse(lVal,rVal);
            }
            case LexAnn.TT_LGR:{
                return evalGr(lVal,rVal);
            }
            case LexAnn.TT_LGRE:{
                return evalGre(lVal,rVal);
            }
            case LexAnn.TT_MOD:{
                return evalMod(lVal,rVal);
            }
            case LexAnn.TT_LAND:{
                return evalAnd(lVal,rVal);
            }
            case LexAnn.TT_LOR:{
                return evalOr(lVal,rVal);
            }
        }

        return null;
    }

    //addition
    private Object evalPlus(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            return new Integer(((Integer)lVal).intValue()
            +((Integer)rVal).intValue());
        } else if (lVal instanceof Double && rVal instanceof Double){
            return new Double(((Double)lVal).doubleValue()
            +((Double)rVal).doubleValue());
        } else if (lVal instanceof String || rVal instanceof String){
            return new String(lVal.toString()+rVal.toString());
        } else if (lVal instanceof Double && rVal instanceof Integer){
            return new Double(((Double)lVal).doubleValue()
            +((Integer)rVal).intValue());
        } else if (lVal instanceof Integer && rVal instanceof Double){
            return new Double(((Integer)lVal).intValue()
            +((Double)rVal).doubleValue());
        } else {
            parseError("Type Mismatch for operator +");
        }

        return null;
    }

    //subtraction
    private Object evalMinus(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            return new Integer(((Integer)lVal).intValue()
            -((Integer)rVal).intValue());
        } else if (lVal instanceof Double && rVal instanceof Double){
            return new Double(((Double)lVal).doubleValue()
            -((Double)rVal).doubleValue());
        } else if (lVal instanceof Double && rVal instanceof Integer){
            return new Double(((Double)lVal).doubleValue()
            -((Integer)rVal).intValue());
        } else if (lVal instanceof Integer && rVal instanceof Double){
            return new Double(((Integer)lVal).intValue()
            -((Double)rVal).doubleValue());
        } else {
            parseError("Type Mismatch for operator -");
        }

        return null;
    }

    //multiplication
    private Object evalMult(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            return new Integer(((Integer)lVal).intValue()
            *((Integer)rVal).intValue());
        } else if (lVal instanceof Double && rVal instanceof Double){
            return new Double(((Double)lVal).doubleValue()
            *((Double)rVal).doubleValue());
        } else if (lVal instanceof Double && rVal instanceof Integer){
            return new Double(((Double)lVal).doubleValue()
            *((Integer)rVal).intValue());
        } else if (lVal instanceof Integer && rVal instanceof Double){
            return new Double(((Integer)lVal).intValue()
            *((Double)rVal).doubleValue());
        } else {
            parseError("Type Mismatch for operator *");
        }

        return null;
    }

    //modulus
    private Object evalMod(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            return new Integer(((Integer)lVal).intValue()
            %((Integer)rVal).intValue());
        } else {
            parseError("Type Mismatch for operator %");
        }

        return null;
    }

    //Logical AND
    private Object evalAnd(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            boolean b1,b2;
            b1=((Integer)lVal).intValue()!=0;
            b2=((Integer)rVal).intValue()!=0;
            if (b1 && b2){
                return FS_TRUE;
            }else{
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator &&");
        }

        return null;
    }

    //Logical Or
    private Object evalOr(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            boolean b1,b2;
            b1=((Integer)lVal).intValue()!=0;
            b2=((Integer)rVal).intValue()!=0;
            if (b1 || b2){
                return FS_TRUE;
            }else{
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator ||");
        }

        return null;
    }

    //division
    private Object evalDiv(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            return new Integer(((Integer)lVal).intValue()
            /((Integer)rVal).intValue());
        } else if (lVal instanceof Double && rVal instanceof Double){
            return new Double(((Double)lVal).doubleValue()
            /((Double)rVal).doubleValue());
        } else if (lVal instanceof Double && rVal instanceof Integer){
            return new Double(((Double)lVal).doubleValue()
            /((Integer)rVal).intValue());
        } else if (lVal instanceof Integer && rVal instanceof Double){
            return new Double(((Integer)lVal).intValue()
            /((Double)rVal).doubleValue());
        } else {
            parseError("Type Mismatch for operator /");
        }

        return null;
    }

    //logical equal
    private Object evalEq(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            if (lVal.equals(rVal)){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof Double && rVal instanceof Double){
            if (lVal.equals(rVal)){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof String && rVal instanceof String){
            if (lVal.equals(rVal)){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof FSObject){
            if (lVal.equals(rVal)){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (rVal instanceof FSObject){
            if (rVal.equals(lVal)){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator ==");
        }

        return null;
    }

    //<
    private Object evalLs(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            if (((Integer)lVal).intValue()<((Integer)rVal).intValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof Double && rVal instanceof Double){
            if (((Double)lVal).doubleValue()<((Double)rVal).doubleValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof String && rVal instanceof String){
            if (((String)lVal).compareTo((String)rVal)<0) {
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator <");
        }
        return null;
    }

    //<=
    private Object evalLse(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            if (((Integer)lVal).intValue()<=((Integer)rVal).intValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof Double && rVal instanceof Double){
            if (((Double)lVal).doubleValue()<=((Double)rVal).doubleValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof String && rVal instanceof String){
            if (((String)lVal).compareTo((String)rVal)<=0) {
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator <=");
        }
        return null;
    }

    //>
    private Object evalGr(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            if (((Integer)lVal).intValue()>((Integer)rVal).intValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof Double && rVal instanceof Double){
            if (((Double)lVal).doubleValue()>((Double)rVal).doubleValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof String && rVal instanceof String){
            if (((String)lVal).compareTo((String)rVal)>0) {
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator >");
        }
        return null;
    }

    //>=
    private Object evalGre(Object lVal,Object rVal) throws FSException{
        if (lVal instanceof Integer && rVal instanceof Integer){
            if (((Integer)lVal).intValue()>=((Integer)rVal).intValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof Double && rVal instanceof Double){
            if (((Double)lVal).doubleValue()>=((Double)rVal).doubleValue()){
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else if (lVal instanceof String && rVal instanceof String){
            if (((String)lVal).compareTo((String)rVal)>=0) {
                return FS_TRUE;
            } else {
                return FS_FALSE;
            }
        } else {
            parseError("Type Mismatch for operator >=");
        }
        return null;
    }

    //logical inequallity
    private Object evalNEq(Object lVal,Object rVal) throws FSException{
        if ( evalEq( lVal, rVal ) == FS_TRUE )
        {
            return FS_FALSE;
        } else {
            return FS_TRUE;
        }
    }

/*    private void printWTree(ETreeNode node){
        while(node.parent!=null){
            node=node.parent;
        }
        printETree(node);
    } */

/*    private void printETree(ETreeNode node){

        System.out.println(node);
        if (node.left!=null){
            System.out.print("Left");
            printETree(node.left);
        }
        if (node.right!=null){
            System.out.print("Right");
            printETree(node.right);
        }
    } */

    private void parseIf() throws IOException, FSException,RetException {
        Integer val;
        int depth;
        boolean then=false;


        getNextToken();
        Object obj=parseExpr();
        if (obj instanceof Integer)
            val=(Integer)obj;
        else {
            if (obj instanceof FSObject) obj=((FSObject)obj).getObject();
            if (obj instanceof Boolean)
                val=((Boolean)obj).booleanValue() ? FS_TRUE : FS_FALSE;
            else if (obj instanceof Integer) {
                // test needed a second time 'cause it may have been an FSObject before
                val=(Integer)obj;
            } else {
                parseError("If condition needs to be Integer");
                return; // just to make sure the compiler doesn't complain
                // as we know parseError throws an exception (stupid compiler)
            }
        }

        //handle the one line if-then construct
        if (tok.ttype==LexAnn.TT_THEN){
            getNextToken();
            //is this a single line then (or just a optional then)
            if (tok.ttype!=LexAnn.TT_EOL){
                //single line if then construct - run separately
                //tok.pushBack();
                if (val.intValue()!=0){
                    parseStmt();
                } else {
                    //consume to EOL
                    while(tok.ttype!=LexAnn.TT_EOL){
                        getNextToken();
                    }
                }
                then=true;
            }
        }

        if (!then){
            if (val.intValue()!=0) {
                getNextToken();
                while((tok.ttype!=LexAnn.TT_EIF)&&
                (tok.ttype!=LexAnn.TT_ELSE) &&
                (tok.ttype!=LexAnn.TT_EOF)&&
                (tok.ttype!=LexAnn.TT_ELSIF)) {
                    //run the body of the if
                    parseStmt();
                    getNextToken();
                }
                if (tok.ttype==LexAnn.TT_ELSE || tok.ttype==LexAnn.TT_ELSIF) {
                    //skip else clause -
                    //have to do this taking into acount nesting
                    depth=1;
                    do {
                        getNextToken();
                        if (tok.ttype==LexAnn.TT_IF) depth++;
                        if (tok.ttype==LexAnn.TT_EOF)
                            parseError("can't find endif");
                        if (tok.ttype==LexAnn.TT_EIF) depth--;

                        //A then could indicate a one line
                        //if - then construct, then we don't increment
                        //depth
                        if (tok.ttype==LexAnn.TT_THEN) {

                            getNextToken();
                            if (tok.ttype!=LexAnn.TT_EOL){
                                depth--;
                            }
                            tok.pushBack();
                        }

                    } while(depth>0);
                    getNextToken();
                } else {
                    getNextToken();
                }

            } else {
                //skip to else clause
                depth=1;
                do {
                    getNextToken();
                    if (tok.ttype==LexAnn.TT_IF )depth++;
                    if (tok.ttype==LexAnn.TT_EOF)
                        parseError("can't find endif");
                    if ((tok.ttype==LexAnn.TT_EIF)) depth--;
                    if ((tok.ttype==LexAnn.TT_ELSE || tok.ttype==LexAnn.TT_ELSIF )&& depth==1) depth--;
                    //A then could indicate a one line
                    //if - then construct, then we don't increment
                    //depth
                    if (tok.ttype==LexAnn.TT_THEN) {

                        getNextToken();
                        if (tok.ttype!=LexAnn.TT_EOL){
                            depth--;
                        }
                        tok.pushBack();
                    }

                } while(depth>0);


                if (tok.ttype==LexAnn.TT_ELSE) {
                    getNextToken();
                    getNextToken();
                    //run else clause

                    while(tok.ttype!=LexAnn.TT_EIF) {
                        parseStmt();
                        getNextToken();
                    }
                    getNextToken();
                } else if (tok.ttype==LexAnn.TT_ELSIF){
                	parseIf();
                } else {
                    getNextToken();
                }
            }
        }

    }

    private void parseWhile() throws IOException, FSException,RetException {
        //parses the while statement

        Integer val;
        boolean looping = true;
        int startLine;
        //int endPos;
        int depth;

        startLine=code.getCurLine();

        while ( looping )
        {
            getNextToken();     // a 'while' you would imagine
            Object obj=parseExpr();
            if (obj instanceof Integer)
                val=(Integer)obj;
            else {
                if (obj instanceof FSObject) obj=((FSObject)obj).getObject();
                if (obj instanceof Boolean)
                    val=((Boolean)obj).booleanValue() ? FS_TRUE : FS_FALSE;
                else if (obj instanceof Integer) {
                    // test needed a second time 'cause it may have been an FSObject before
                    val=(Integer)obj;
                } else {
                    parseError("While condition needs to be Integer");
                    return; // just to make sure the compiler doesn't complain
                    // as we know parseError throws an exception (stupid compiler)
                }
            }
            getNextToken();

            if (val.intValue()==0)
            {
                looping = false;
            }
            else
            {
                while( (tok.ttype!=LexAnn.TT_EWHILE) && (tok.ttype!=LexAnn.TT_EOF) )
                {
                    parseStmt();
                    getNextToken();
                }

                //reset to start of while loop....
                code.setCurLine(startLine);
                resetTokens();
            }
        }
        //skip to endwhile
        depth=1;
        do {
            getNextToken();
            if (tok.ttype==LexAnn.TT_WHILE) depth++;
            if (tok.ttype==LexAnn.TT_EWHILE) depth--;
            if (tok.ttype==LexAnn.TT_EOF)
                parseError("can't find endwhile");
        } while (depth>0);

        getNextToken();
    }


    private void parseVarDef() throws IOException, FSException {

        String name;
        int type=tok.ttype;

        do {
            getNextToken();
            if (tok.ttype!=LexAnn.TT_WORD) {
                parseError("Expected variable name identifier,");
            }

            name=(String)tok.value;

            switch (type){
                case LexAnn.TT_DEFINT: {
                    addVar(name,FS_FALSE);
                    break;
                }
                case LexAnn.TT_DEFSTRING:{
                    addVar(name,new String(""));
                    break;
                }
                case LexAnn.TT_DEFDOUBLE:{
                    addVar(name,new Double(0));
                    break;
                }
                case LexAnn.TT_DEFOBJECT:{
                    addVar(name,new FSObject());
                    break;
                }
            }

            getNextToken();
            if (tok.ttype==LexAnn.TT_EQ){
                getNextToken();
                setVar(name,parseExpr());
            } else if (tok.ttype!=',' && tok.ttype!=LexAnn.TT_EOL) {
                parseError("Expected ','");
            }

        } while (tok.ttype!=LexAnn.TT_EOL);

    }

    //format an error message and throw FSException
    private void parseError(String s) throws FSException {

        // set up our error block
        error=new String[6];
        error[0]=s;
        error[1]=(new Integer(code.getCurLine())).toString();
        error[2]=code.getLineAsString();
        error[3]=tok.toString();;
        error[4]=vars.toString();
        if (gVars!=null) error[5]=(gVars==null)?"":gVars.toString();

        // build the display string
        s="\n\t"+s+"\n"+getContext();

        throw new FSException(s);
    }

    /**
     * get the current context (executed line, variables etc)
     * @return
     */
    public String getContext() {
        int l=code.getCurLine();
        String s="\t\t at line:" + l + " ";
        if (l>-1) {
            s+="\n\t\t\t  "+code.getLineAsString(l-2);
            s+="\n\t\t\t  "+code.getLineAsString(l-1);
            s+="\n\t\t\t> "+code.getLineAsString(l)+" <";
            s+="\n\t\t\t  "+code.getLineAsString(l+1);
            s+="\n\t\t\t  "+code.getLineAsString(l+2);
            s=s+ "\n\t\t current token:" + tok.toString();;
            s=s+ "\n\t\t Variable dump:" + vars;
            if (gVars!=null) {
                s=s+ "\n\t\t Globals:" + gVars;
            }
        } else s+="\n\t\t\t> "+tok.getLine()+" <";

        return s;
    }

    //return the error block
    String[] getError() {
        return error;
    }

    // Other non SM related routines

    //misc token access routines
    private void getNextToken() throws IOException {
        if (tok.ttype==LexAnn.TT_EOL) {
            if (code.getCurLine() < maxLine) {
                code.setCurLine(code.getCurLine()+1);
                tok.setString(code.getLine());
                tok.nextToken();
            } else {
                tok.ttype=LexAnn.TT_EOF; //the only place this gets set
            }
        } else {
            tok.nextToken();
        }
    }

    private void resetTokens() throws IOException {
        tok.setString(code.getLine());
        tok.nextToken();
    }


    //variable access routines
    void addVar(String name,Object value) throws FSException {

        if (vars.containsKey(name)) {
            parseError("Already defined in this scope: " + name );
        }
        vars.put(name,value);
    }


    public Object getVar(String name) {
        if (subParser!=null) return subParser.getVar(name);
        if (vars.containsKey(name)) {
            return vars.get(name);
        } else {
            if (gVars!=null) {
                if (gVars.containsKey(name)) {
                    return gVars.get(name);
                }
            }
        }

        // variable not found, try extensions
        try
        {
            return host.getVarEntry( name, null );
        }
        catch ( Exception e )
        {}

        return null; //shouldn't get here
    }


    // setVar allows assigning objects only when the objects are of the same type
    // OR you can assign a String, Integer or Double to an FSObject if the contained object already
    // has the same type. Note that you can change the type of the embedded object for "object" variables.
    public void setVar(String name,Object val) throws FSException {

        Object obj;

        if (val==null) parseError("set variable "+name+" with null value");

        if (subParser!=null) {
            subParser.setVar(name,val);
            return;
        }

        if ( (obj=vars.get(name)) != null ) {
            if (val.getClass() != obj.getClass()) {
                //special case for FSObject allow asignment of either same
                //class or _any_ class if FSObject is already null
                //also allow assignment of null to any FSObject
                if (obj instanceof FSObject) {
                    if (((FSObject)obj).getObject()==null){
                        val=new FSObject(val);
                    }
                    else if(((FSObject)obj).getObject().getClass()==val.getClass()){
                        val=new FSObject(val);
                    }
                    else {
                        parseError("Incompatible types");
                    }
                }
                else{
                    parseError("Incompatible types");
                }
            }
            vars.remove(name);
            vars.put(name,val);
        } else if ( (obj=gVars.get(name)) != null ) {
            if (val.getClass() != obj.getClass()) {
                parseError("Incompatible types");
            }
            gVars.remove(name);
            gVars.put(name,val);
        }

    }

    public boolean hasVar(String name) {
        if (subParser!=null) return subParser.hasVar(name);
        if (gVars==null) {
            return vars.containsKey(name);
        } else {
            return vars.containsKey(name) ||
            gVars.containsKey(name);
        }
    }

}





