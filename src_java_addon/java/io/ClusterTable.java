// ClusterTable.java

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

/* History
 *
 *
 */

package java.io;

import com.sun.kjava.*;

class ClusterTable {

    static final int CLUSTER_SIZE_LD = 9;
    static final int CLUSTER_SIZE = 1 << CLUSTER_SIZE_LD;

    static final int DB_CREATOR = 0x4B415754;
    static final int DB_TYPE = 0x46494c45;

    static final byte [] EMPTY_TABLE = {0, 0, 0, 0};

    // achtung: vier nullbytes am anfang reserviert fuer "future use"
    static byte [] freeClusters; 


    static com.sun.kjava.Database db;

    int recNo;
    byte [] table;
    boolean dirty = false;
    int fileSize;
    
    ClusterTable (File file, boolean isDirectory) {
	
	// 1. care about database opening / creation
	
	//System.out.println ("ClusterTable (" + file.localName 
	//		    +", "+isDirectory +")");

	if (db == null) {
	    //    System.out.println ("opening fileDb!");

	    db = new com.sun.kjava.Database 
		(DB_TYPE, DB_CREATOR, com.sun.kjava.Database.READWRITE);

	    //	    System.out.println ("opened!");
	
	    if (!db.isOpen()) {

		//System.out.println ("creating filedb!");

		com.sun.kjava.Database.create
		    (0, "TrantorFileDB", DB_CREATOR, DB_TYPE, false);

		db = new com.sun.kjava.Database 
		    (DB_TYPE, DB_CREATOR, com.sun.kjava.Database.READWRITE);

		db.addRecord (EMPTY_TABLE); // root dir fat
		db.addRecord (EMPTY_TABLE); // free cluster table

		// System.out.println ("created!");
	    }

	    freeClusters = db.getRecord (1);

	    //System.out.println 
	    //("free records: " + (freeClusters.length-4));
	}

	// care about cluster table creation if not existing

	//System.out.println ("bef.validate");

	file.validate ();

	//System.out.println ("aft.validate");

	if (file.clusterTableRecord == -1) {
	    
	    //System.out.println ("creating new file!");
	    
	    file.clusterTableRecord = alloc (false);
	    
	    //System.out.println ("allocated cluster: "+file.clusterTableRecord);

	    //db.setRecord (file.clusterTableRecord, EMPTY_TABLE);

	    //System.out.println ("record set, updating dir!");

	    RandomAccessFile dir = null;

	    try {
		dir = new RandomAccessFile (file.parent, "rw");
	    } 
	    catch (IOException ioe) {}

	    //System.out.println ("dir read!");
	    
	    try {
		dir.seek ((int) dir.length ());
	    

		dir.writeChars ((isDirectory ? "D" : "F") 
				+ file.clusterTableRecord + " " + file.localName);
		
		
		dir.close ();
	    }
	    catch (IOException ioe) {}
	    
	    //System.out.println ("dir updated!");
	}

	recNo = file.clusterTableRecord;

	//System.out.println ("reading table");

	table = db.getRecord (recNo);
	
	//System.out.println ("table size: "+table.length);

	fileSize = (((((int) table [0]) & 0x0ff) << 24)
		    + ((((int) table [1]) & 0x0ff) << 16)
		    + ((((int) table [2]) & 0x0ff) << 8)
		    + ((((int) table [3]) & 0x0ff)));
    }


    public int alloc (boolean big) {
	
	if (freeClusters.length > 4) {
	    int i = freeClusters.length - 2; 
	    int freeNo = (((int) freeClusters [i] & 255) << 8)
		+ ((int) freeClusters [i+1] & 255);

	    byte [] newFree = new byte [i];
	    System.arraycopy (freeClusters, 0, newFree, 0, i);
	    freeClusters = newFree;
	    db.setRecord (1, newFree);

	    if (big) db.setRecord (freeNo, new byte [CLUSTER_SIZE]);
	    //	    else db.setRecord (freeNo, EMPTY_TABLE);

	    return freeNo;
	}
	
	if (big) db.addRecord (new byte [CLUSTER_SIZE]);
	else db.addRecord (EMPTY_TABLE);

	return db.getNumberOfRecords ()-1;
    }


    public void delete () {

	// da table in den ersten 4 bytes size enthaelt, muessen
	// diese beim kopieren der freien clusters uebersprungen 
	// werden...
	
	byte [] newFreeClusters = 
	    new byte [freeClusters.length + table.length - 2];

	int oldSize = freeClusters.length;

	System.arraycopy (freeClusters, 0, 
			  newFreeClusters, 0, oldSize);
	
	newFreeClusters [oldSize] = (byte) ((recNo >> 8) & 255);
	newFreeClusters [oldSize+1] = (byte) (recNo & 255);
	
	db.setRecord (recNo, EMPTY_TABLE);

	int cnt = (table.length - 4) / 2;
	for (int i = 0; i < cnt; i++) 
	    writeCluster (i, EMPTY_TABLE);

	System.arraycopy (table, 4,
			  newFreeClusters, oldSize + 2, table.length - 4);

	freeClusters = newFreeClusters;
	
	db.setRecord (1, freeClusters);

	table = null;
	recNo = -1;
    }

    
    public void flush () {
	if (!dirty) return;

	table [0] = (byte) ((fileSize >> 24) & 255);
	table [1] = (byte) ((fileSize >> 16) & 255);
	table [2] = (byte) ((fileSize >> 8) & 255);
	table [3] = (byte) (fileSize & 255);
    
	//System.out.println ("replacing table rnr:"+recNo);

	db.setRecord (recNo, table);

	dirty = false;
    }
	


    byte [] readCluster (int index) {

	int rnr = (((((int) table [index*2+4]) & 0x0ff) << 8)
		   + ((((int) table [index*2+5]) & 0x0ff)));
	
	//System.out.println ("readCluster "+index +" rnr "+rnr);
	
	return db.getRecord (rnr);
    }


    /** may be changed to x-aligned allocation */
    
    public void setFileSize (int newSize) {

	if (fileSize == newSize) return;

	fileSize = newSize;
	dirty = true;
	
	int clusters = (fileSize == 0) 
	    ? 0 
	    : (((fileSize-1) >> CLUSTER_SIZE_LD) + 1);

	int newTableSize = 4 + clusters * 2;

	
	// FIXME: am ende ueberheangende cluster freigeben

	if (newTableSize <= table.length) return;

	//System.out.println 
	//  ("nwsz: "+fileSize+" cl: "+clusters 
	//   +" tz:"+table.length+ " nwtz: "+newTableSize);


	byte [] newTable = new byte [newTableSize];

	System.arraycopy 
	    (table, 0, newTable, 0, 
	     Math.min (table.length, newTableSize));

	for (int i = table.length; i < newTableSize; i += 2) {
	    int newRecNo = alloc (true);
	    
	    //System.out.println ("appended recno: "+newRecNo);

	    newTable [i] = (byte) ((newRecNo >> 8) & 255);
	    newTable [i+1] = (byte) (newRecNo & 255);
	}

	table = newTable;
	
	//System.out.println ("table replaced");

	flush ();
    }


    


    void writeCluster (int index, byte [] buf) {

	int rnr = (((((int) table [index*2+4]) & 0x0ff) << 8)
		   + ((((int) table [index*2+5]) & 0x0ff)));
	
	//System.out.println ("writeCluster "+index +" rnr "+rnr);

	db.setRecord (rnr, buf);
    }

}
