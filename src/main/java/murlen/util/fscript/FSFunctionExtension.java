package murlen.util.fscript;
import java.util.ArrayList;

/**
 * <p>FSFunctionExtension - simple function extension to plug into FSFastExtension
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
 */
public interface FSFunctionExtension {
    
    /**
     * <p>callFunction is called whenever a function call is made in FScript to a
     * function not defined withing hte script itself</p>
     * @param name the name of the function
     * @param params an array list of parameters passed to the function
     * @return the return value (Object) of the call
     **/
    public Object callFunction(String name, ArrayList params)
    throws FSException;
    
    
}
