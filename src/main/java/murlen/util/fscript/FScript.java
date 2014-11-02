package murlen.util.fscript;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * <p>Femto Script - This is the main FScript package class</p>
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
 * 02.08.2002 added support for FSParserExtension
 * 15.03.2003 getContext()
 */

public class FScript implements FSExtension{
    
    private Parser parser;
    private LineLoader code;
    private ArrayList extensions;
    
    /** Constructor */
    public FScript() {
        
        parser=new Parser(this);
        code=new LineLoader();
        parser.setCode(code);
        extensions=new ArrayList();
        
    }
    /**
     * Loads FScript parser with text from an InputStreamReader
     * @param is the input stream
     */
    public void load(Reader is) throws IOException {
        code.load(is);
    }
    
    /**
     * Load an individual line into the parser, intended for
     * document processing applications
     * @param line the line to load
     */
    public void loadLine(String line) {
        code.addLine(line);
    }
    
    /**
     *Registers language extensions
     *@param extension the extension to register
     **/
    public void registerExtension(FSExtension extension){
        if (extension instanceof FSParserExtension)
            ((FSParserExtension)extension).setParser(parser);
        extensions.add(extension);
    }
    
    /**
     *Removes a previously registered extenison
     *@param extension the extension to remove
     **/
    public void unRegisterExtension(FSExtension extension){
        extensions.remove(extension);
    }
    
    /**
     * Run the parser over currently loaded code
     *@return any return value of the script's execution (will be one of
     *FScript's supported type objects, Integer,String,Double)
     */
    public Object run() throws IOException, FSException {
        //reset the internal variable state
        parser.reset();
        return parser.parse(0,code.lineCount()-1);
    }
    
    public Object evaluateExpression(String expr) throws IOException, FSException {
        if (!expr.startsWith("return ")) expr="return "+expr;
        return parser.parse(expr);
    }
    
    /**
     * Resets the internal code store
     */
    public void reset(){
        code.reset();
        parser.reset();
    }
    
    /**
     * Continues execution from current point - only really
     * useful in a document processing application where you may
     * wish to add code, execute, add some more code..etc..
     *@return any return value of the script's execution (will be one of
     *FScript's supported type objects, Integer,String,Double)
     */
    public Object cont() throws IOException,FSException {
        if (code.getCurLine()==0){
            return run();
        }
        else {
            return parser.parse(code.getCurLine()+1,code.lineCount()-1);
        }
    }
    
    /**
     * Returns more details on any error states, indicated by
     * FSExceptions.
     * @return String, see below <br>
     * s[0]=the error text <BR>
     * s[1]=the line number <BR>
     * s[2]=the line text <BR>
     * s[3]=the current token <BR>
     * s[4]=a variable dump (current scope) <BR>
     * s[5]=a global variable dump (only if currnent scope is not global <BR>
     */
    public String[] getError() {
        return parser.getError();
    }
    
    /**
     * Override this method to allow external access to variables
     * in your code.
     * @param name the name of the variable the parser is requesting
     * e.g
     * add this...
     * <br>
     * if (name.equals("one") { return new Integer(1) }
     * <br>
     * to allow the code
     * <br>
     * a=one
     * <br>
     * to work in FScript
     * @return Object - currently expected to be String or Integer
     */
    public Object getVar(String name)throws FSException {
        throw new FSUnsupportedException(name);
    }
    
    /**
     * Override this method to allow external access to variables
     * in your code.
     *<p>As getVar(String name) but allows an index variable to be
     *passed so code such as :
     * name=list[2]
     * is possible
     * @param name the name of the variable the parser is requesting
     * @return Object - currently expected to be String, Integer or Double
     */
    public Object getVar(String name,Object index)throws FSException {
        throw new FSUnsupportedException(name);
    }
    
    /**
     *Entry point for parser (checks against extensions)
     **/
    Object getVarEntry(String name,Object index) throws FSException {
        int n;
        
        for(n=0;n<extensions.size();n++){
            FSExtension extension=(FSExtension)extensions.get(n);
            
            try {
                if (index==null){
                    return extension.getVar(name);
                } else {
                    return extension.getVar(name,index);
                }
                
            }
            catch (FSUnsupportedException e){
                //Do nothing continue looping through extensions
            }
        }
        
        //make call to (hopefully) subclass
        if (index==null){
            return getVar(name);
        } else {
            return getVar(name,index);
        }
        
    }
    
    /**
     * Logical inverse of getVar
     * @param name the variable name
     * @param value the value to set it to
     */
    public void setVar(String name,Object value) throws FSException {
        throw new FSUnsupportedException(name);
    }
    
    /**
     * Logical inverse of getVar (with index)
     * @param name the variable name
     * @param index the index into the 'array'
     * @param value the value to set it to
     */
    public void setVar(String name,Object index,Object value)
    throws FSException {
        throw new FSUnsupportedException(name);
    }
    
    
    /**
     *Entry point for parser (checks against extensions)
     **/
    void setVarEntry(String name,Object index,Object value) throws FSException {
        
        int n;
        boolean handled=false;
        
        for(n=0;n<extensions.size();n++){
            FSExtension extension=(FSExtension)extensions.get(n);
            
            try {
                if (index==null){
                    extension.setVar(name,value);
                    handled=true;
                } else {
                    extension.setVar(name,index,value);
                    handled=true;
                }
                
            }
            catch (FSUnsupportedException e){
                //Do nothing continue looping through extensions
            }
        }
        
        //make call to (hopefully) subclass
        if (!handled){
            setVar(name,value);
        }
        
    }
    
    /**
     * Override this call to implement custom functions
     * See the BasicIO class for an example
     *
     * @param name the function name
     * @param params an ArrayList of parameter values
     * @return an Object, currently expected to be Integer or String
     */
    public Object callFunction(String name, ArrayList params) throws FSException {
        throw new FSUnsupportedException(name);
    }
    
    /**
     *Entry point for parser (checks against extensions)
     **/
    Object callFunctionEntry(String name,ArrayList params)
    throws FSException {
        
        int n;
        
        for(n=0;n<extensions.size();n++){
            FSExtension extension=(FSExtension)extensions.get(n);
            
            try {
                return extension.callFunction(name,params);
            }
            catch (FSUnsupportedException e){
                //Do nothing continue looping through extensions
            }
        }
        
        //make call to (hopefully) subclass
        
        return callFunction(name,params);
    }
    
    /**
     *Sets a variable in script space = the value passed in - the variable
     *must be have the correct type - note that if the varialble is not defined in the
     *script, calls will be made to subclass setVar methods - therefore this method
     *should be used with caution from within an overriden setVar.
     *@param name the name of the variable
     *@param value the value to set variable to (String,Integer)*/
    public final void setScriptVar(String name,Object value) throws FSException{
        parser.setVar(name,value);
    }
    
    /**
     *Gets a variable in script space note that if the varialble is not defined in the
     *script, calls will be made to subclass getVar methods - therefore this method
     *should be used with caution from within an overriden getVar.
     *@param name the name of the variable
     *@return the value of the variable (String,Integer)*/
    public final Object getScriptVar(String name) throws FSException{
        return parser.getVar(name);
    }
    
    /**Calls a function in the script - note that if the function is not defined calls
     *will be made to the subclass callFunction methods - therefore this method should
     *be used with caution from within an overriden callFunction.
     *@param name the name of the function
     *@param params the parameters to pass (must be correct type and number)
     *@return the return value of the function (String,Integer)
     */
    public final Object callScriptFunction(String name,ArrayList params) throws
    IOException,FSException{
        return parser.callFunction(name,params);
    }
    
    /**
     * get the current context (executed line, variables etc)
     * @return
     */
    public String getContext() {
        return parser.getContext();
    }
    
}
