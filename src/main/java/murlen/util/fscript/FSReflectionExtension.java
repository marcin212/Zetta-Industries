package murlen.util.fscript;

import murlen.util.fscript.introspection.IntrospectorBase;

import java.util.ArrayList;
import java.lang.reflect.Method;

/**
 * <p>ReflectionExtension - general extension for object access where either
 * the class handles the processing (if it implements FSExtension),
 * or reflection is used.
 * </b>
 * <p>
 * <I>Copyright (C) 2002-2003 </I></p>
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
 * @author murlen
 *
 * modifications by Joachim Van der Auwera
 * 05.08.2002 started (Joachim)
 * 08.09.2002 major changes to allow overloading of params for methods/fields
 *      and use of static methods/fields. (murlen)
 * 08-10.10.2002 use Velocity introspection code for better handling of native
 *      types and improved speed
 * 18.11.2002 imporved exception handling
 * 28.01.2003 fixed problem with return handling on objectMethod()
 * 15.03.2003 pluggable exception handling
 * 28.02.2004 make nulls typed
 */

public class FSReflectionExtension implements FSParserExtension {
    Parser parser;
    IntrospectorBase introspector=new IntrospectorBase();
    ExceptionHandler exceptionHandler=new ExceptionHandler() {
        public void handle(String name, Exception exc) throws FSException {
            throw new FSException("Error calling method "+name+" "+exc.getMessage());
        }
    };
    FSObject nullObj=new FSObject(null);

    public void setParser(Parser parser) {
        this.parser=parser;
    }

    public Object getVar(String name) throws FSException {
        int pos=name.indexOf('.');
        if (pos>0) {
            String oname=name.substring(0, pos);
            name=name.substring(pos+1);
            Object object=parser.getVar(oname);
            if (object!=null&&object instanceof FSObject) {
                object=((FSObject)object).getObject();
                if (object==null) throw new FSException("variable "+oname+" is null");

                return getObjectVar(object, name);
            }
        }
        throw new FSUnsupportedException();
    }

    public void setVar(String name, Object value) throws FSException {
        int pos=name.indexOf('.');
        if (pos>0) {
            String oname=name.substring(0, pos);
            name=name.substring(pos+1);
            Object object=parser.getVar(oname);
            if (object!=null&&object instanceof FSObject) {
                object=((FSObject)object).getObject();
                if (object==null) throw new FSException("variable "+oname+" is null");
                setObjectVar(object, name, value);
            } else {
                throw new FSUnsupportedException();
            }
        } else {
            throw new FSUnsupportedException();
        }
    }

    public Object getVar(String name, Object index) throws FSException {
        // @todo ...
        return null;
    }

    public void setVar(String name, Object index, Object value) throws FSException {
        // @todo ...
    }

    public Object callFunction(String name, ArrayList params) throws FSException {
        int pos=name.indexOf('.');

        if (pos>0) {
            String oname=name.substring(0, pos);
            name=name.substring(pos+1);
            Object object=parser.getVar(oname);

            // we only work with FSObjects
            if (object!=null&&object instanceof FSObject) {
                object=((FSObject)object).getObject();

                if (object==null) throw new FSException("variable "+oname+" is null");

                return objectMethod(object, name, params.toArray());
            }
        } else if (name.equals("create")) {
            // create a new object
            ArrayList cParams=(ArrayList)params.clone();
            cParams.remove(0);
            return createObject(params.get(0).toString(), cParams);


        } else if (name.equals("getClass")) {
            // get a class object - this lets us call static methods etc.
            return new FSObject(getClass((String)params.get(0)));
        } else if (name.equals("null")) {
            // get a typed null object
            return new FSObject(null, getClass((String)params.get(0)));
        }
        throw new FSUnsupportedException();
    }

    /*
     * Called to invoke a method of a given object - tries to be fairly
     * comprehensive when it comes to checking types/etc so we don't get
     * bad calls
     */
    protected Object objectMethod(Object target, String methodName, Object params[])
    throws FSException {

        Method method=null;
        try {
            Class c;
            if (target instanceof Class){
                c=(Class)target;
            } else {
                c=target.getClass();
            }
            method=introspector.getMethod(c, methodName, params);
        } catch (Exception ex) {
            throw new FSException("Error calling method "+methodName+ex.getMessage());
        }
        if (method==null) throw new FSReflectionException("Error method "+methodName+" does not exists or ambigous");
        try {
            // rebuild argument list, unwrap all the FSObjects
            Object params2[]=new Object[params.length];
            for (int i=params.length-1 ; i>=0 ; i--) {
                Object obj=params[i];
                if (obj instanceof FSObject) obj=((FSObject)obj).getObject();
                params2[i]=obj;
            }
            return normalizeObj(method.invoke(target, params2), method.getReturnType());
        } catch (Exception ex) {
            exceptionHandler.handle(methodName, ex);
            return nullObj;
        }
    }


    private Object createObject(String className, ArrayList params)
    throws FSException {
        try {
            Class c;

            // create the class
            c=getClass(className);

            if (c==null)
                return null;

            // build array of our params
            Object[] o=new Object[params.size()];
            Object tmpObj;

            for (int i=0 ; i<o.length ; i++) {
                //unwrap fsobjects
                tmpObj=params.get(i);
                if (tmpObj instanceof FSObject) {

                    o[i]=((FSObject)tmpObj).getObject();
                } else {
                    o[i]=tmpObj;
                }
            }

            java.lang.reflect.Constructor[] constructors=c.getDeclaredConstructors();

            // find appropriate constructor
            for (int i=0 ; i<constructors.length ; i++) {
                Class[] classes=constructors[i].getParameterTypes();

                if (checkMethods(classes, o)) {
                    // use to create object
                    return normalizeObj(constructors[i].newInstance(o), c);
                }
            }
        } catch (Exception e) {
            throw new FSException("Error Creating new object "+e.getMessage());
        }

        return null;
    }

    /*
     * Returns a reference to a Class object of type <name>
     */
    private Class getClass(String name) throws FSException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        } catch (Exception e) {
            throw new FSException("Error getting class "+name+" "+e.getMessage());
        }

    }

    /*
     * Sets a field of an object (also tries java brans style set)
     */
    protected void setObjectVar(Object o, String name, Object value) throws FSException {
        Object arr[]=new Object[1];
        arr[0]=value;
        try {
            objectMethod(o, "set"+name, arr);
        } catch (FSReflectionException ex) {
            // unwrap fsobject
            if (value instanceof FSObject){
                value=((FSObject)value).getObject();
            }

            // method not found, try direct field access
            Class c;
            //handle use of static classes
            if (o instanceof Class){
                //just cast to a class
                c=(Class)o;
            } else {
                //it is not a class so we need to get the class
                c=o.getClass();
            }
            java.lang.reflect.Field f=null;

            try {
                //does the class have this field?
                f=c.getField(name);
            } catch (NoSuchFieldException e){}

            if (f!=null) {
                try {
                    f.set(o,value);
                }
                catch (Exception e){
                    throw new FSException("Could not access " + name + " " + e.getMessage());
                }
            } else {
                // oops field not found
                throw ex;
            }
        }
    }

    /*
     * Returns a field of an object (also tries java beans style get)
     */
    protected Object getObjectVar(Object o, String name) throws FSException {
        try {
            return objectMethod(o, "get"+name, new Object[0]);
        } catch (FSReflectionException ex) {
            // try direct field access
            Class c;

            //handle static classes
            if (o instanceof Class){
                c=(Class)o;
            } else {
                c=o.getClass();
            }

            java.lang.reflect.Field f=null;

            try {
                f=c.getField(name);
            } catch (NoSuchFieldException e){}

            if (f!=null){
                try {
                    return normalizeObj(f.get(o), f.getType());
                }
                catch (Exception e){
                    throw new FSException("Could not access " + name + " " + e.getMessage());
                }
            } else {
                // oops field not found
                throw ex;
            }

        }
    }

    // used to check that parameters of calling
    // object and method call match
    private boolean checkMethods(Class[] c, Object[] o) {

        int n,len;

        // easy exit not the same length params
        if (c.length!=o.length) {
            return false;
        }

        // check that methods have same types
        len=c.length;
        for (n=0 ; n<len ; n++) {

            if (!c[n].isInstance(o[n])&&
            !(c[n].equals(Integer.TYPE)&&o[n] instanceof Integer)&&
            !(c[n].equals(Double.TYPE)&&o[n] instanceof Double)) {
                return false;
            }
        }

        return true;

    }

    /*
     * Ensures the right type is passed back i.e. Integer,String,Double
     * or FSObject
     */
    private Object normalizeObj(Object o, Class c) {
        // Return the right type...
        if (o instanceof Integer || o instanceof String || o instanceof Double) {
            return o;
        } else {
            return new FSObject(o, c);
        }
    }

    /**
     * set the exception handler routine which should be called for all exceptions caused by the
     * referenced getter and setter methods (not the exceptions while trying to call, but only the
     * exceptions thrown by the called code).
     * @param eh the exception handler which should be used
     */
    public void setExceptionHandler(ExceptionHandler eh) {
        exceptionHandler=eh;
    }

    public interface ExceptionHandler {
        public void handle(String name, Exception exc) throws FSException;
    }

    // marker for method not found exceptions
    public class FSReflectionException extends FSException {
        public FSReflectionException() {}
        public FSReflectionException(String msg) { super(msg); }
    }

}

