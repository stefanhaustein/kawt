// kAWT - Kilobyte Abstract Window Toolkit
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

package de.kawt.impl.kjava;

import de.kawt.impl.*;
import javax.microedition.rms.*;
import com.sun.kjava.Database;
import java.io.*;

public class RecordStoreImpl extends AbstractRecordStore {
    
    Database stores;
    Database openedStore;

    int storesCreator = 0x4B524D53; // KRMS
    int storesType = getTypeIDInt("Strs");

    int openedCreator;
    int openedType;
    String openedName;


    public void initRecordMem (String recordStoreName, boolean createIfNotAvaliable) {
	
	stores = new com.sun.kjava.Database (storesType, storesCreator, com.sun.kjava.Database.READWRITE);
	  
	if (!stores.isOpen()) {
	    com.sun.kjava.Database.create (0, "kAWT-RmsStores", storesCreator, storesType, false);
	    stores = new com.sun.kjava.Database (storesType, storesCreator, com.sun.kjava.Database.READWRITE);
	}
	  
	// DB nach store durchsuchen.
	
	boolean found = false;
	int noOfRecs = stores.getNumberOfRecords ();
	for (int i = 0; i < noOfRecs; i++) {
	    byte[] buf = stores.getRecord (i);

	    openedCreator = getCreatorIDFromBuffer (buf);
	    openedType = getTypeIDFromBuffer (buf);
	    openedName = getDBNameFromBuffer (buf);
	    
	    if (recordStoreName.equals (openedName)) {
		found = true;
		break;
	    }
	}
	  
	if (found) {
	    openedStore = new com.sun.kjava.Database (openedType, openedCreator, com.sun.kjava.Database.READWRITE);  
	    if (!openedStore.isOpen()) {
		com.sun.kjava.Database.create (0, "kAWT-RS:"+recordStoreName, openedCreator, openedType, false);
	    }
	} 
	else {
	    String noOfStoresHex = Integer.toHexString (stores.getNumberOfRecords ());
	    String crString = "";
	    
	    if (noOfStoresHex.length () == 1) {
		crString = "000"+noOfStoresHex;
	    } 
	    else if (noOfStoresHex.length () == 2) {
		crString = "00"+noOfStoresHex;
	    } 
	    else if (noOfStoresHex.length () == 3) {
		crString = "0"+noOfStoresHex;
	    } 
	    else {
		crString = noOfStoresHex;
	    }

	    //System.out.println ("New Store TypeID <"+crString+"> created.");
	    
	    int cr = getTypeIDInt("KRMS");
	    int ty = getTypeIDInt(crString);

	    com.sun.kjava.Database.create (0, "kAWT-RS:"+recordStoreName, cr, ty, false);
	    openedStore = new com.sun.kjava.Database (ty, cr, com.sun.kjava.Database.READWRITE);  
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream ();
	    baos.write (intToByteArray (cr), 0, 4);
	    baos.write (intToByteArray (ty), 0, 4);
	    baos.write (recordStoreName.getBytes (), 0, recordStoreName.length ());
	    stores.addRecord (baos.toByteArray()); 
	}
    }


    private void checkOpen () throws RecordStoreNotOpenException {
	if (openedStore == null)
	    throw new RecordStoreNotOpenException ();
    } 


    private void checkId (int index) throws RecordStoreException {
	checkOpen ();
	if (index < 1 || index > openedStore.getNumberOfRecords ()) 
	    throw new InvalidRecordIDException 
		("Id "+index+" not valid");
    }



    private int getCreatorIDFromBuffer (byte[] buffer) {
	return (((((int) buffer [0]) & 0x0ff) << 24)
		+ ((((int) buffer [1]) & 0x0ff) << 16)
		+ ((((int) buffer [2]) & 0x0ff) << 8)
		+ ((((int) buffer [3]) & 0x0ff)));
    }

    
    private int getTypeIDFromBuffer (byte[] buffer) {
	return (((((int) buffer [4]) & 0x0ff) << 24)
		+ ((((int) buffer [5]) & 0x0ff) << 16)
		+ ((((int) buffer [6]) & 0x0ff) << 8)
		+ ((((int) buffer [7]) & 0x0ff)));
    }

    
    private String getDBNameFromBuffer (byte[] buffer) {
	System.out.println ("DBbuffer = "+buffer.length );
	return new String (buffer, 8, buffer.length-8); 
    }


    private int getTypeIDInt(String typeID){
        return((typeID.charAt(0) << 24) +
	       (typeID.charAt(1) << 16) +
	       (typeID.charAt(2) <<  8) +
	       (typeID.charAt(3)));
    }


    private byte[] intToByteArray (int integer) {
	byte[] result = new byte[4];
	result [0] = (byte) ((integer >> 24) & 255);
	result [1] = (byte) ((integer >> 16) & 255);
	result [2] = (byte) ((integer >> 8) & 255);
	result [3] = (byte) (integer & 255);
	return result;
    }


    
    /*******************************************************************/
    /*******************************************************************/
    /*******************************************************************/

    
    public int addRecord (byte[] data, int offset, int length) 
	throws RecordStoreNotOpenException,
	       RecordStoreException,
	       RecordStoreFullException {

	checkOpen ();
	
	if (offset > 0 || length != data.length) {
	    byte[] newData = new byte[length];
	    System.arraycopy (data, offset, newData, 0, length);
	    openedStore.addRecord (newData);
	}
	else {
	    openedStore.addRecord (data);
	}

	return openedStore.getNumberOfRecords ();
    }
    
    
    public void addRecordListener (RecordListener listener) {
	throw new RuntimeException ("not implemented");
    }
    
    
    public void closeRecordStore ()
	throws RecordStoreNotOpenException,
	       RecordStoreException {
    
	checkOpen ();
	  
	stores.close ();
	openedStore.close ();
    }
    
    
    public void deleteRecord (int recordId) 
	throws RecordStoreNotOpenException,
	       InvalidRecordIDException,
	       RecordStoreException {
	
	checkId (recordId);
	openedStore.deleteRecord (recordId-1);
    }
    
    
    /**     
     * Returns an enumeration for traversing a set of records in the record store in an optionally specified order.
     */
    //public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated) {
    //return null;
    //}

    
    public long getLastModified () throws RecordStoreNotOpenException {
	checkOpen ();
	throw new RuntimeException ("not impl");
    }

    
    public String getName () throws RecordStoreNotOpenException {
	checkOpen ();
	return openedName;
    }
    
    
    public int getNextRecordID () 
	throws RecordStoreNotOpenException, RecordStoreException {
	
	return getNumRecords ()+1;
    }

    
    public int getNumRecords () throws RecordStoreNotOpenException {
	checkOpen ();
	return openedStore.getNumberOfRecords ();
    }

    
    public byte[] getRecord (int recordId) 
	throws RecordStoreNotOpenException,
	       InvalidRecordIDException,
	       RecordStoreException {
	
	checkId (recordId);
	return openedStore.getRecord (recordId-1);
    }
    

    public int getRecord (int recordId, byte[] buffer, int offset) 
	throws RecordStoreNotOpenException,
	       InvalidRecordIDException,
	       RecordStoreException,
	       ArrayIndexOutOfBoundsException {
	
	byte [] ba = getRecord (recordId);
	System.arraycopy (ba, 0, buffer, offset, ba.length);
	return ba.length;
    }

        
    public int getRecordSize (int recordId) 
	throws RecordStoreNotOpenException,
	       InvalidRecordIDException,
	       RecordStoreException {
	
	return getRecord (recordId).length;
    }

    
    public int getSize () throws RecordStoreNotOpenException {
	checkOpen ();
	
	int size = 0;
	int noOfRecs = openedStore.getNumberOfRecords ();
	for (int i = 0; i < noOfRecs; i++) {
	    byte[] buffer = openedStore.getRecord (i);
	    size += buffer.length;
	}
	
	return size;;
    }

    
    public int getSizeAvailable () throws RecordStoreNotOpenException {
	return Integer.MAX_VALUE;
    }

    
    public int getVersion () throws RecordStoreNotOpenException {
	throw new RuntimeException ("not implemented");
    }

  
    public void removeRecordListener (RecordListener listener) {
	throw new RuntimeException ("not implemented");
    }

    
    public void setRecord (int recordId, byte[] data, int offset, int length) 
	throws RecordStoreNotOpenException,
	       InvalidRecordIDException,
	       RecordStoreException,
	       RecordStoreFullException {

	checkId (recordId);
	
	if (offset > 0 || length != data.length) {
	    byte[] newData = new byte[length];
	    System.arraycopy (data, offset, newData, 0, length);
	    openedStore.setRecord (recordId-1, newData);
	}
	else {
	    openedStore.setRecord (recordId-1, data);
	}
    }
}





