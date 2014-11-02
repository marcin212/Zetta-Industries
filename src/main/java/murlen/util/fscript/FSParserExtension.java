package murlen.util.fscript;
/**
 * <p>FSParserExtension - an extension which needs a reference to the parser
 * - USE WITH CAUTION</p>
 * <p>
 * <I>Copyright (C) 2002</I></p>
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
 * @version
 */
public interface FSParserExtension extends FSExtension {
    
    /**
     * set the parser which is used to parse the executed Fscript code
     * @param parser
     */
    public void setParser(Parser parser);
    
}
