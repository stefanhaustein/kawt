// FileOutputStream.java
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

/** A file output stream is an output stream for writing data to a File or to a FileDescriptor. */

public class FileOutputStream extends OutputStream {

    RandomAccessFile raf;


    /**
     * Creates a FileInputStream by opening a connection to an actual 
     * file, the file named by the File object file in the file system.
     * @param  file - the file to be opened for reading.
     * @exception IOException if the file exists but is a directory
     * rather than a regular file, does not exist but cannot be
     * created, or cannot be opened for any other reason 
     */

    public FileOutputStream (File file) throws IOException {
	
	raf = new RandomAccessFile (file, "w");
    }


    /**
     * Closes this file output stream and releases any system 
     * resources associated with this stream.
     * @exception IOException if an I/O error occurs.
     */

    public void close () throws IOException {
	raf.setLength (raf.getFilePointer ());
	raf.close ();
    }
    

    /**
     * Writes the specified byte to this file output stream.
     * @param b the byte to be written.
     * @exception IOException if an I/O error occurs.
     */

    public void write (int b) throws IOException {
	raf.write ((byte) b);
    }

    
    /**
     * Writes len bytes from the specified byte array starting at offset off to this file output stream.
     * @param b the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @exception IOException if an I/O error occurs.
     */

    public void write (byte [] buf, int off, int len) throws IOException {
	raf.write (buf, off, len);
    }
}

/*
 * $Log: FileOutputStream.java,v $
 * Revision 1.2  2001/09/03 12:22:27  mkroll
 * Removes Files[] listFiles() from java.io.File.
 * Added CVS-Log tags to FileInputStream and FileOutputStream.
 * Updated the make_* files somewhat.
 *
 */
