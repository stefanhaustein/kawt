// RandomAccessFile.java
//
// 2000-09-08 MK Added new Licensetext
// 2000-07-24 MK added Documentaion        
//            changed writeLine to writeChars 
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

/** Instances of this class support both reading and writing to a random access file.*/

public class RandomAccessFile {

    static int openFiles;
    byte [] buf;

    ClusterTable clusterTable;

    int clusterPos;
    int fragmentPos;
    int pos;
    boolean dirty = false;
    
    
    /**
     * Creates a random access file stream to read from, and optionally to write to, a file with the specified name.
     * @param name the system-dependent filename.
     * @param mode the access mode.
     * @exception FileNotFoundException if the file exists but is a
     * directory rather than a regular file, or cannot be opened or
     * created for any other reason 
     */

    public RandomAccessFile (String name, String mode) throws IOException {
	this (new File (name), mode);
    }


    /**
     * Creates a random access file stream to read from, and optionally to write to, the file specified by the File argument.
     * @param file the file object.
     * @param mode the access mode.
     * @exception FileNotFoundException if the file exists but is a
     * directory rather than a regular file, or cannot be opened or
     * created for any other reason 
     */

    public RandomAccessFile (File file, String mode) throws IOException {

	openFiles++;

	clusterTable = new ClusterTable 
	    (file, mode.equals ("d"));

	seek (0);
    }

    
    /**
     * Closes this random access file stream and releases any system resources associated with the stream.
     * @exception IOException  if an I/O error occurs.
     */

    public void close () throws IOException {
	flush ();

	buf = null;
	fragmentPos = 0;
	
	if (--openFiles == 0) {

	    ClusterTable.db.close ();
	    ClusterTable.db = null;
	}
    }


    void flush () {
	if (dirty) {
	    clusterTable.writeCluster (clusterPos, buf);
	    clusterTable.flush ();
	}
    }

    
    /**
     * Returns the current offset in this file.
     * @return the offset from the beginning of the file, in bytes, at which the next read or write occurs.
     * @exception IOException if an I/O error occurs.
     */

    public int getFilePointer () throws IOException {
	return pos;
    }


    /** 
     * Returns the length of this file.
     * @return the length of this file, measured in bytes.
     * @exception IOException if an I/O error occurs.
     */

    public int length () throws IOException {
	return clusterTable.fileSize;
    }


    /**
     * Reads a byte of data from this file.<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
     * !!!!!!!! Method in J2SE is "public int read()" !!!!!!!!!!!!<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */

    public byte read (){
	
	if (pos >= clusterTable.fileSize)
	    throw new RuntimeException ("READ PAST END"); 

	if (fragmentPos >= ClusterTable.CLUSTER_SIZE) 
	    try {
		seek (pos);
	    }
	    catch (IOException ioe) {}

	pos++;
	return buf [fragmentPos++];
    }


    /**
     * Reads up to len bytes of data from this file into an array of bytes.
     * @param b  the buffer into which the data is read.
     * @param off  the start offset of the data.
     * @param len  the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or -1
     * if there is no more data because the end of the file has been
     * reached.
     * @exception IOException if an I/O error occurs.  
     */

    public int read (byte [] b, int start, int len) throws IOException {
	if (len == 0) return 0;

	if (fragmentPos >= ClusterTable.CLUSTER_SIZE) 
	    seek (pos);

	len = Math.min (len, ClusterTable.CLUSTER_SIZE - fragmentPos);
	len = Math.min (len, clusterTable.fileSize - pos);

	System.arraycopy (buf, fragmentPos, b, start, len);

	pos += len;
	fragmentPos += len;

	return len == 0 ? -1 : len;
    }


    /**
     * Writes len bytes from the specified byte array starting at offset off to this file.
     * @param b  the data.
     * @param off  the start offset in the data.
     * @param len  the number of bytes to write.
     * @exception IOException if an I/O error occurs. 
     */

    public void write (byte [] b, int offset, int len) throws IOException {

	if (pos + len > clusterTable.fileSize) 
	    clusterTable.setFileSize (pos + len);

	while (len > 0) {
	    if (fragmentPos >= ClusterTable.CLUSTER_SIZE) 
		seek (pos);

	    int cnt = Math.min 
		(len, ClusterTable.CLUSTER_SIZE - fragmentPos);

	    System.arraycopy (b, offset, buf, fragmentPos, cnt);

	    dirty = true;
	    len -= cnt;
	    pos += cnt;
	    fragmentPos += cnt;
	    offset += cnt;
	}	
    }


    /**
     * Reads the next line of text from this file. 
     * @return the next line of text from this file, or null if end of file is encountered before even one byte is read.
     * @exception IOException if an I/O error occurs. 
     */

    public String readLine () throws IOException {
	
	StringBuffer sb = new StringBuffer ();

	while (true) {
	    if (pos >= clusterTable.fileSize) break;

	    byte b = read ();
	    if (b == 10) break;
	    
	    sb.append ((char) b);
	}
	
	return sb.toString ();
    }


    /**
     * Sets the file-pointer offset, measured from the beginning of this file, at which the next read or write occurs.<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
     * !!!!!!!! Method in J2SE is "public void seek (long pos)" !!!!!!!!!!!!<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param pos the offset position, measured in bytes from the beginning of the file, at which to set the file pointer.
     * @exception IOException if an I/O error occurs.
     */

    public void seek (int pos) throws IOException {

	flush ();

	if (pos > clusterTable.fileSize) 
	    throw new RuntimeException ("seek past eof");

	clusterPos = pos >> ClusterTable.CLUSTER_SIZE_LD;
        fragmentPos = pos - clusterPos * ClusterTable.CLUSTER_SIZE;

	this.pos = pos;

	if (this.pos == clusterTable.fileSize && fragmentPos == 0) 
	    buf = new byte [ClusterTable.CLUSTER_SIZE];
	else 
	    buf = clusterTable.readCluster (clusterPos);
    }


    /**
     * Sets the length of this file.
     * @param newLength  The desired length of the file
     * @exception IOException If an I/O error occurs
     */

    public void setLength (int newLength) throws IOException {
	if (newLength != clusterTable.fileSize) {
	    clusterTable.setFileSize (newLength);
	    dirty = true;
	}
    }


    /**
     * Writes the specified byte to this file.<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!<br>
     * !!!!!!!! Method in J2SE is "public void write (int b)" !!!!!!!!!!!!<br>
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
     * @param b  the byte to be written.
     * @exception IOException If an I/O error occurs
     */
    
    public void write (byte b) throws IOException {

	if (pos >= clusterTable.fileSize) 
	    clusterTable.setFileSize (pos+1);
	
	if (fragmentPos >= ClusterTable.CLUSTER_SIZE) 
	    seek (pos); 

	buf [fragmentPos++] = b;
	pos++;
	dirty = true;
    }


    /**
     * Writes a string to the file as a sequence of characters.
     * @param s  a String value to be written.
     * @exception IOException If an I/O error occurs
     */
    
    public void writeChars (String s) throws IOException {
	for (int i = 0; i < s.length (); i++) 
	    write ((byte) s.charAt (i));

	write ((byte) 10);
    }
}











