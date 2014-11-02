package murlen.util.fscript;
import java.util.ArrayList;
/**
 *<p>This class implements a bare FSExtension - subclassing from this will help
 * prevent errors caused by not throwing FSUnsupportedException from empty
 * set/getVar and callFunction methods.</p>
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
 *
 **/

public abstract class BasicExtension implements FSExtension{
    
    public BasicExtension() {
    }
    
    public Object callFunction(String name, ArrayList params) throws FSException{
        throw new FSUnsupportedException(name);
    }
    
    public Object getVar(String name) throws FSException{
        throw new FSUnsupportedException(name);
    }
    
    public void setVar(String name,Object value) throws FSException{
        throw new FSUnsupportedException(name);
    }
    
    public Object getVar(String name,Object index) throws FSException{
        throw new FSUnsupportedException(name);
    }
    
    public void setVar(String name,Object index,Object value) throws FSException{
        throw new FSUnsupportedException(name);
    }
    
}

