
package murlen.util.fscript;

import java.io.*;
import java.util.*;

/**
 * <p>BasicIO - an simple IO Subclass of FScript</p>
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
 * @author murlen\
 *CVSTEST
 
 */
public class BasicIO extends FScript {
    
    private Object files[];
    
    /**Constructor*/
    public BasicIO() {
        super();
        files=new Object[25];
    }
    
    /**
     *<p> Overridden from FScript implements the following FScript functions </p>
     *
     * <p> note that this only provides very basic IO facilities,
     * line by line read/write
     * to files, and stdio read write.  There is a maximum of 25 open files</p>
     * <p> <b>(void) println(param...)</b> - write to stdout -
     * takes variable parameter list </p>
     * <p> <b>string readln() </b> - reads a string from stdin </p>
     * <p> <b>int open(string filename,string mode) </b> -
     * opens a file 'filename' for
     * reading (mode="r") or writing (mode="w") returns an integer which is
     * used in future calls. Returns -1 on >25 files opened </p>
     * <p> <b>string read(fp) </b> - reads one line from previously openened file
     * </p>
     * <p> <b>void write(fp,param...) - writes concatination of all params to one
     * line of file </p>
     */
    public Object callFunction(String name,ArrayList param) throws FSException {
        
        //(void) println(param.....)
        if (name.equals("println")) {
            int n;
            String s="";
            for(n=0;n<param.size();n++) {
                s=s+ param.get(n);
            }
            System.out.println(s);
        }
        //string readln()
        else if (name.equals("readln")) {
            try {
                return new BufferedReader(
                new InputStreamReader(System.in)).readLine();
                
            } catch (IOException e)  {
                throw new FSException(e.getMessage());
            }
        }
        //int open(string file,string mode)
        else if (name.equals("open")) {
            int n;
            
            try {
                for(n=0;n<25;n++) {
                    if (files[n]==null) {
                        if (((String)param.get(1)).equals("r")) {
                            files[n]=new BufferedReader(
                            new FileReader((String)param.get(0)));
                            break;
                        } else if (((String)param.get(1)).equals("w"))  {
                            files[n]=new BufferedWriter(
                            new FileWriter((String)param.get(0)));
                            break;
                        } else {
                            throw new FSException(
                            "open expects 'r' or 'w' for modes");
                        }
                    }
                }
            } catch (IOException e)  {
                throw new FSException(e.getMessage());
            }
            if (n<25) return new Integer(n);
            else return new Integer(-1);
        }
        //(void)close(int fp)
        else if (name.equals("close")) {
            int n;
            n=((Integer)param.get(0)).intValue();
            if (files[n]==null) {
                throw new FSException("Invalid file number passed to close");
            }
            try {
                if (files[n] instanceof BufferedWriter) {
                    ((BufferedWriter)files[n]).close();
                } else {
                    ((BufferedReader)files[n]).close();
                }
                files[n]=null;
            } catch (IOException e) {
                throw new FSException(e.getMessage());
            }
        }
        //(void) write(params....)
        else if (name.equals("write")) {
            int n;
            String s="";
            for(n=1;n<param.size();n++) {
                s=s+ param.get(n);
            }
            n=((Integer)param.get(0)).intValue();
            if (files[n]==null) {
                throw new FSException("Invalid file number passed to write");
            }
            if (!(files[n] instanceof BufferedWriter)) {
                throw new FSException("Invalid file mode for write");
            }
            try {
                ((BufferedWriter)files[n]).write(s,0,s.length());
                ((BufferedWriter)files[n]).newLine();
            } catch (IOException e) {
                throw new FSException(e.getMessage());
            }
        }
        //string read(int fp)
        else if (name.equals("read")) {
            int n;
            String s;
            n=((Integer)param.get(0)).intValue();
            if (files[n]==null) {
                throw new FSException("Invalid file number passed to read");
            }
            if (!(files[n] instanceof BufferedReader)) {
                throw new FSException("Invalid file mode for read");
            }
            try {
                s=((BufferedReader)files[n]).readLine();
                //dodge eof problems
                if (s==null) s="";
                return s;
            } catch (IOException e) {
                throw new FSException(e.getMessage());
            }
        }
        //int eof(fp)
        else if (name.equals("eof")) {
            int n;
            n=((Integer)param.get(0)).intValue();
            if (files[n]==null) {
                throw new FSException("Invalid file number passed to eof");
            }
            BufferedReader br=(BufferedReader)files[n];
            try {
                br.mark(1024);
                if (br.readLine()==null) {
                    return new Integer(1);
                } else {
                    br.reset();
                    return new Integer(0);
                }
            } catch (IOException e) {
                throw new FSException(e.getMessage());
            }
        } else {
            super.callFunction(name,param);
        }
        return new Integer(0);
    }
}
