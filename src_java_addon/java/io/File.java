// File.java
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

import java.util.*;

/** 
 * An abstract representation of file and directory pathnames based on
 * com.sun.kjava.Database. 
 * <br>
 * <br>
 * <b>The kAWT FileDB Format:</b>
 * <br>
 * The IO extension of kAWT contains a hierarchical file system
 * simulation based on a regular Palm memory database. Storing clusters
 * on a Palm is done inside a database called "TrantorFileDB" with
 * creatorID/typeID = KAWT/FILE
 * <br>
 * <br>
 * <b>Record Structure:</b>
 * <br>
 * The first record (#0) in the FileDB contains the cluster table of the
 * root directory. The second record contains a list of unused records.
 * <br>
 * <br>
 * <b>Files:</b>
 * <br>
 * Files consist of a cluster table and one or more data records. The
 * data records have a fixed size of 512 bytes.
 * <br>
 * <br>
 * <b>Cluster Tables:</b>
 * <br>
 * The first four bytes of the cluster table contain the current file
 * length in Motorola format. The file length is followed by (filesize /
 * 512) two-byte entries. These entries are pointers to the 512-byte
 * records containing the actual file data.
 * <br>
 * <br>
 * <b>Directories:</b>
 * <br>
 * Directories are organized like simple files. The directory file
 * contains a list of filenames and cluster table record pointers. The
 * directory entires start with a letter (D/F), denoting the type of the
 * entry. If the letter is a "D", the entry points to a directory. If the
 * letter is a "F", the entry points to a simple file. The type letter is
 * followed by a space and the local name of the file. The file name is
 * followed by a space. The space is followed by the record number of the
 * cluster table of the file. The record number is encoded in decimal
 * format. The entry is terminated by a newline character (#10, 0x0A).
 *
 */

public class File {

    String givenName;
    
    int valid = -1;

    File parent;
    String localName;

    /** -1 means file does not exist. */

    int clusterTableRecord;
    boolean isDirectory;

    static int structureChangeCounter = 0;


    /** The system-dependent default name-separator character,
        represented as a string for convenience. */

    public static String separator = "/";
    

    /** The system-dependent default name-separator character. */
    
    public static char separatorChar = '/';


    File (int clusterTableRecord, File parent, 
	  String localName, boolean isDirectory) {

	givenName = concatNames (parent.givenName, localName);
	valid = structureChangeCounter;

	this.clusterTableRecord = clusterTableRecord;
	this.parent = parent;
	this.localName = localName;
	this.isDirectory = isDirectory;
    }

   
    /** Creates a new File instance from a parent abstract pathname
     * and a child pathname string.
 
     @param parent - The parent abstract pathname
     @param child - The child pathname string */
    
    public File (File parent, String child) {
	givenName = concatNames (parent.toString (), child);
    }

    
    /** Creates a new File instance from a parent pathname string and
        a child pathname string.

        @param parent - The parent pathname string
        @param child - The child pathname string */

    public File (String parent, String child) {
	givenName = concatNames (parent, child);
    }


    /** Creates a new File instance by converting the given 
	pathname string into an abstract pathname.
     
	@param pathname - A pathname string */


    public File (String pathname) {
	givenName = pathname;
    }


    /** Deletes the file or directory denoted by this 
	abstract pathname.
     
        @return true if and only if the file or directory is
        successfully deleted; false otherwise */

    public boolean delete () {
	
	validate ();

	if (clusterTableRecord == -1 
	    || parent == null) {
	    //System.out.println ("del "+localName + ": doesnt exist!");
	    return false; // doesnt exist
	}
	
	if (isDirectory && listFiles ().length != 0) {
	    //System.out.println ("del "+localName + ": dir not empty!");
	    return false; // not empty
	}

	File [] parentFiles = parent.listFiles ();

	if (parentFiles == null) {
	    //System.out.println ("del "+localName + ": parent file list null");
	    return false; // no parent dir (?!?)
	}

	try {

	    RandomAccessFile parentFile = new RandomAccessFile (parent, "d");

	    for (int i = 0; i < parentFiles.length; i++) {
		File current = parentFiles [i];
		if (!current.localName.equals (localName))
		    parentFile.writeChars 
			((current.isDirectory ? "D" : "F") 
			 + current.clusterTableRecord + " " 
			 + current.localName);
	    }
	    
	    //System.out.println ("deleted, parent dir new length: "
	    //	    +parentFile.getFilePointer ());
	    
	    parentFile.setLength (parentFile.getFilePointer ());
	    parentFile.close ();

	} 
	catch (IOException ioe) {}
	
	new ClusterTable (this, true).delete ();
	
	clusterTableRecord = -1;
	structureChangeCounter++;
	
	return true;
    }
    
    /** tries to set all fields correctly by 
	validating the parent and then searching
	for the local file name. */


    void validate () {

	if (valid == structureChangeCounter) return;

	if (givenName.equals ("/")) { 
	    clusterTableRecord = 0;
	    isDirectory = true;
	    localName = "/";
	}
	else {
	    if (givenName.endsWith ("/"))
		givenName = givenName.substring (0, givenName.length () - 1);

	    int cut = givenName.lastIndexOf ('/');
	    
	    if (cut < 1) parent = new File ("/");
	    else parent = new File (givenName.substring (0, cut));

	    parent.validate ();

	    localName = givenName.substring (cut+1);

	    if (localName.equals (".") || localName.equals ("..")) {
		
		File src = (localName.equals ("..") && parent.parent != null)
		    ? parent.parent
		    : parent;

		parent = src.parent;
		localName = src.localName;
		isDirectory = src.isDirectory;
		clusterTableRecord = src.clusterTableRecord;
		valid = src.valid;
	    }
	    else {
		clusterTableRecord = -1;

		File [] files = parent.listFiles ();
	    
		if (files != null) { 
		    for (int i = 0; i < files.length; i++) {
			if (files [i].localName.equals (localName)) { 
			    isDirectory = files [i].isDirectory;
			    clusterTableRecord = files [i].clusterTableRecord;
			    parent = files [i].parent;
			    break;
			}
		    }
		}
	    }
	}
	
	valid = structureChangeCounter;
    }


    static String concatNames (String s1, String s2) {
	if (!s1.endsWith ("/")) s1 = s1 + "/";

	if (s2.startsWith ("/")) s2 = s2.substring (1);

	return s1 + s2;
    }

    
    /**
     * Returns the canonical pathname string of this abstract pathname.
     * @return The canonical pathname string denoting the same file or 
     *         directory as this abstract pathname
     * @exception IOException If an I/O error occurs, which is
     * possible because the construction of the canonical pathname may
     * require filesystem queries */

    public String getCanonicalPath () throws IOException {
	validate ();

	if (parent == null) return "/";

	return concatNames (parent.getCanonicalPath (), localName);
    }


    /**
     * Returns the name of the file or directory denoted by this
     * abstract pathname.
     * @return The name of the file or directory denoted by this
     * abstract pathname, or the empty string if this pathname's name
     * sequence is empty 
     */

    public String getName () {
	return localName;
    }


    /** Returns a boolean determining if the file exists. */

    public boolean exists () {
	validate ();
	return clusterTableRecord != -1;
    }

    
    /**
     * Tests whether the file denoted by this abstract pathname is a directory.
     * @return true if and only if the file denoted by this abstract pathname 
     *         exists and is a directory; false otherwise
     */

    public boolean isDirectory () {
   
	validate ();
	return isDirectory;
    }
  

    /**
     * Returns an array of strings naming the files and directories in
     * the directory denoted by this abstract pathname.
     * @return An array of strings naming the files and directories in
     * the directory denoted by this abstract pathname. The array will
     * be empty if the directory is empty. Returns null if this
     * abstract pathname does not denote a directory, or if an I/O
     * error occurs.  
     */

    public String [] list () {
	validate ();

	String[] result = null;
	
	if (clusterTableRecord == -1) return null;

	try {

	    RandomAccessFile file = new RandomAccessFile (this, "rw");
	    Vector dir = new Vector ();
	    
	    //System.out.println ("list - file length: "+file.length ());
	    
	    while (file.getFilePointer () < file.length ()) {
		
		String entry = file.readLine ();
		//System.out.println ("list-entry: "+entry);
		int cut = entry.indexOf (' '); 
		dir.addElement (entry.substring (cut+1));
	    }
	    
	    file.close ();
	    /*String []*/ result = new String [dir.size ()];
	    
	    for (int i = 0; i < result.length; i++) 
		result [i] = (String) dir.elementAt (i);
	}
	catch (IOException ioe) {}
	
	return result;
    }
    

    /**
     * Returns the length of the file denoted by this abstract pathname.
     * @return The length, in bytes, of the file denoted by this
     * abstract pathname, or 0L if the file does not exist 
     */

    public long length () {
	validate ();
	if (clusterTableRecord == -1) return 0;
	    
	return new ClusterTable (this, isDirectory).fileSize;
    }


    /**
     * Returns an array of abstract pathnames denoting the files in 
     * the directory denoted by this abstract pathname. 
     * @return An array of abstract pathnames denoting the files and
     * directories in the directory denoted by this abstract
     * pathname. The array will be empty if the directory is
     * empty. Returns null if this abstract pathname does not denote a
     * directory, or if an I/O error occurs.  
     */

    private File [] listFiles () {
	File[] result = null;
	
	
	validate ();
	
	if (clusterTableRecord == -1) return null;
      
	try {
	    
	    RandomAccessFile file = new RandomAccessFile (this, "d");
	    Vector dir = new Vector ();
	    
	    while (file.getFilePointer () < file.length ()) {
		String entry = file.readLine ();
		int cut = entry.indexOf (' ');
		
		dir.addElement (new File 
		    (Integer.parseInt (entry.substring (1, cut)),
		     this,
		     entry.substring (cut+1),
		     entry.charAt (0) == 'D'));
	    }
	    
	    file.close ();
	    //File [] 
	    result = new File [dir.size ()];
	    
	    for (int i = 0; i < result.length; i++) 
		result [i] = (File) dir.elementAt (i);
	    
	} 
	catch (IOException ioe) {}
	
	return result;
	
    }
    
    
    /**
     * Creates the directory named by this abstract pathname.
     * @return true if and only if the directory was created, along
     * with all necessary parent directories; false otherwise
     */
    
    public boolean mkdir () {
	validate ();
	try {
	    
	    if (clusterTableRecord != -1) return false;
	    RandomAccessFile file = new RandomAccessFile (this, "d");
	    file.close ();
	    structureChangeCounter++;
	}
	catch (IOException ioe) {}
	return true;
    }

    
    /**
     * Returns the pathname string of this abstract pathname. This is
     * just the string returned by the getPath() method.
     *
     * @return The string form of this abstract pathname 
     */

    public String toString () {
	try {
	    return getCanonicalPath ();
	}
	catch (IOException e) {
	    throw new RuntimeException ("ioerr:"+e);
	}
    } 
}

/*
 * $Log: File.java,v $
 * Revision 1.2  2001/09/03 12:22:27  mkroll
 * Removes Files[] listFiles() from java.io.File.
 * Added CVS-Log tags to FileInputStream and FileOutputStream.
 * Updated the make_* files somewhat.
 *
 */






