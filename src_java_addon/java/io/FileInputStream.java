// FileInputStream.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-07-24 MK added Documentaion               
//
//#include ..\..\license.txt
//
// kAWT version 0.95
//
// Copyright (C) 1999-2000 by Michael Kroll & Stefan Haustein GbR, Essen
//
// Contact: kawt@kawt.de
// General Information about kAWT is available at: http://www.kawt.de
//
// Using kAWT for private and educational and in GPLed open source
// projects is free. For other purposes, a commercial license must be
// obtained. There is absolutely no warranty for non-commercial use.
//
//
// 1. BECAUSE THE PROGRAM IS LICENSED FREE OF CHARGE, THERE IS NO
//    WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE
//    LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
//    HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
//    WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT
//    NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
//    FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE RISK AS TO THE
//    QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE
//    PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY
//    SERVICING, REPAIR OR CORRECTION.
//   
// 2. IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN
//    WRITING WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MAY
//    MODIFY AND/OR REDISTRIBUTE THE PROGRAM AS PERMITTED ABOVE, BE
//    LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL,
//    INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
//    INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
//    DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU
//    OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY
//    OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN
//    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
//   
//    END OF TERMS AND CONDITIONS
// 
//#endinclude


package java.io;

/** A <code>FileInputStream</code> obtains input bytes from a file in a file system.*/

public class FileInputStream extends InputStream {

    RandomAccessFile raf;

    
    /**
     * Creates a <code>FileInputStream</code> by opening a connection
     * to an actual file, the file named by the File object file in
     * the file system.
     * @param File file - the file to be opened for reading.
     * @exception FileNotFoundException if the file does not exist, is
     * a directory rather than a regular file, or for some other
     * reason cannot be opened for reading.  
     */

    public FileInputStream (File file) throws FileNotFoundException {
	if (!file.exists ()) 
	    throw new FileNotFoundException ();

	try {
	    raf = new RandomAccessFile (file, "r");
	} catch (IOException ioe) {
	    throw new RuntimeException (ioe.toString ());
	}
    }


    /**
     * Closes this file input stream and releases any system resources 
     * associated with the stream. 
     * @exception IOException  if an I/O error occurs. 
     */

    public void close () throws IOException {
	raf.close ();
    }


    /**
     * Reads up to len bytes of data from this input stream into an array of bytes.
     * @param b   the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1
     * if there is no more data because the end of the file has been
     * reached.
     * @exception IOException if an I/O error occurs.  
     */
    
    public int read (byte [] buf, int start, int len) throws IOException {
	return raf.read (buf, start, len);
    }


    /**
     * Reads a byte of data from this input stream. This method blocks if 
     * no input is yet available.
     * @return the next byte of data, or -1 if the end of the file is reached.
     * @exception IOException if an I/O error occurs. 
     */

    public int read () throws IOException {
	return (raf.getFilePointer () < raf.length ()) 
	    ? (((int) raf.read ()) & 255)
	    : -1;
    }
}


/*
 * $Log: FileInputStream.java,v $
 * Revision 1.2  2001/09/03 12:22:27  mkroll
 * Removes Files[] listFiles() from java.io.File.
 * Added CVS-Log tags to FileInputStream and FileOutputStream.
 * Updated the make_* files somewhat.
 *
 */
