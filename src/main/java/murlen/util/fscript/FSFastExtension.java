package murlen.util.fscript;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <p>FastExtension - general extension in which other (simple) extensions
 * can be plugged.
 * </p>
 * <p>
 * <I>Copyright (C) 2002 </I></p>
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
 * @author Joachim Van der Auwera
 *
 * modifications by Joachim Van der Auwera
 * 02.08.2002 started
 *
 * 11.08.2002 (murlen) Changed function names to add<X>Extension + java doc
 */
public class FSFastExtension implements FSExtension {
    Hashtable variables=new Hashtable();
    Hashtable functions=new Hashtable();
    Hashtable arrays=new Hashtable();

    /**
     * Add new FSVarExtension to this FastExtension
     * @param name - the name of the variable (must be unique amongst varables)
     * @param var - the FSVarExtension object implementing the varaible
     */
    public void addVarExtension(String name, FSVarExtension var) {
        variables.put(name, var);
    }

    /**
     * Add new FSArrayExtension to this FastExtension
     * @param name - the name of the array (must be unique amongst arrays)
     * @param array - the FSArrayExtension object implementing the array
     */
    public void addArrayExtension(String name, FSArrayExtension array) {
        arrays.put(name, array);
    }

    /**
     * Add new FSFunctionExtension to this FastExtension
     * @param name - the name of the function (must be unique amongs functions)
     * @param fnc - the FSFunctionExtension object implementing the function
     */
    public void addFunctionExtension(String name, FSFunctionExtension fnc) {
        functions.put(name, fnc);
    }

    public Object getVar(String name) throws FSException {
        FSVarExtension var=(FSVarExtension)variables.get(name);
        if (var!=null) return var.getVar(name);
        throw new FSUnsupportedException();
    }

    //FSExtension implementation code below here.

    public void setVar(String name, Object value) throws FSException {
        FSVarExtension var=(FSVarExtension)variables.get(name);
        if (var!=null) {
            var.setVar(name, value);
            return;
        }
        throw new FSUnsupportedException();
    }

    public Object getVar(String name, Object index) throws FSException {
        FSArrayExtension var=(FSArrayExtension)arrays.get(name);
        if (var!=null) return var.getVar(name,index);
        throw new FSUnsupportedException();
    }

    public void setVar(String name, Object index, Object value) throws FSException {
        FSArrayExtension var=(FSArrayExtension)arrays.get(name);
        if (var!=null) {
            var.setVar(name,index,value);
            return;
        }
        throw new FSUnsupportedException();
    }

    public Object callFunction(String name, ArrayList params) throws FSException {
        FSFunctionExtension var=(FSFunctionExtension)functions.get(name);
        if (var!=null) return var.callFunction(name,params);
        throw new FSUnsupportedException();
    }

}