
package murlen.util.fscript;

/**
 * <p>FSObject - representation of a general object type</p>
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
 *
 * modifications
 * 08/09/2002 Added "FSObject Containing..." to toString to ease debugging.
 * 21.10.2002 added equals() for comparisons
 * 25.02.2004 equals() allow compare of object with embedded type X directly with another object of type X
 * 28.02.2004 make null typed
 */

public class FSObject
    extends Object
{
    private Object value;
    private Class nullClass;

    public FSObject() {}

    public FSObject( Object value ) { this.value = value; }

    public FSObject( Object value, Class nClass )
    {
        this.value = value;
        nullClass = nClass;
    }

    public Object getObject() { return value; }

    public void setObject( Object value )
    {
        this.value = value;
        nullClass = null;
    }

    public void setObject( Object value, Class nClass )
    {
        this.value = value;
        nullClass = nClass;
    }

    public Class getNullClass()
    {
        return value == null ? nullClass : value.getClass();
    }

    public boolean equals( Object obj )
    {
        if ( this == obj ) return true;
        if ( obj instanceof FSObject )
        {
            obj = ( (FSObject) obj ).getObject();
        }
        if ( value == obj ) return true;
        if ( value != null ) return value.equals( obj );
        return false;
    }

    public String toString()
    {
        if ( value == null ) return "<null>";
        return "FSObject Containing - " + value.toString();
    }

}
