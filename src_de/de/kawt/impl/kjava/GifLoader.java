package de.kawt.impl.kjava;


import java.io.*;
import java.awt.*;

public class GifLoader  {

    int globalWidth;
    int globalHeight;
    int colorResolution;
    int globalPixelBits;
    int background;
    byte [] globalColorMap;
    byte [] data;
    int pos;
    
    static final int [] interlaceMap
	= { 0,  8,  4, 12,  2,  6, 10,
	    1,  3,  5,  7,  9, 11};
    
    int top;
    int left;
    int width;
    int height;
    int bufPos;
    int bufEnd;
    //int bitPos;
    int resetCode;
    int eofCode;
    
    int pixelBits;
    int colors;
    byte [] colorMap;
    //   byte [] buf;
    byte [] writeBuf;
    int writePos;
    
    int row;
 
    short [] prefix;
    byte [] suffix;
    byte [] stack;
 
    int screenBits = Toolkit.getDefaultToolkit ().getColorModel ().getPixelSize ();
 
    int codeSize;
    int clearCode;
    int firstFree;
    int freeCode;
    int maxCode;
    int minCodeSize;
    int tableSize;
    int tableLimit;
    int x;
    int y;
 
   boolean interlaced;
 
//#ifndef giftest
   ImageImpl image;
//#endif
 
   int current;
   int filled;
   int mask;
    

    /**
     * Used for the putByte() fix of Ivan. Thanx :-)
     */
    private int pass = 0;
    private static final int[] passInc = new int[] {8, 8, 4, 2};
    private static final int[] starts = new int[] {0, 4, 2, 1, 0/* dummy! */};
    


    public GifLoader (byte [] data) {
	//Runtime.getRuntime ().gc ();
	this.data = data;
	if (screenBits > 8) screenBits = 8;
    }
 
//#ifdef giftest 
//#   public void load () {
//#else
    public Image load () {
//#endif
	//	buf = new byte [256];

	//Runtime.getRuntime ().gc ();

 	pos = 6; // skip gif signature
 	//readBytes (buf, 6);
 
 
 	globalWidth = readWord ();
 	globalHeight = readWord ();
 
//#ifdef giftest
//#     System.out.println ("globalWidth: "+globalWidth + " globalHeight: "+globalHeight);
//#endif

	int misc = ((int) data [pos++]) & 0x0ff;

	colorResolution = ((misc >> 4) & 7) + 1;
	globalPixelBits = (misc & 7)  + 1;
	background = ((int) data [pos++]) & 0x0ff;

//#ifdef giftest
//#     System.out.println ("cr: "+colorResolution
//# 			+ " pixelBitsGlobal: "+ globalPixelBits
//# 			+ " bg: " + background);
//# 
//#     if (data [pos++] != 0)
//#       throw new RuntimeException ("GIF Format error: 0 expected");
//#else
 	pos++;
//#endif

	if ((misc & 128) != 0) {
//#ifdef giftest
//#       System.out.println ("Reading global color map");
//#endif
	    globalColorMap = new byte [(1 << globalPixelBits)];
	    readColorMap (globalColorMap, globalColorMap.length);
	}
//#ifdef giftest
//#     else System.out.println ("no global color map");
//#else
	image = new ImageImpl (globalWidth, globalHeight, screenBits);
//#endif
	writeBuf = new byte [globalWidth+16];

	while (true) {
	    int st = ((int)data [pos++]) & 0x0ff;
	    switch (st) {
	    case ',': readImage (); break;
	    case '!': readExtension (); break;
	    case -1:
	    case 0:
	    case ';':
//#ifdef giftest
//# 		return;
//#else
		return image;
//#endif
	    default: throw new RuntimeException ("bst#"+st);
	    }
	}
    }

    void readExtension () {
	pos++; // extension code
	while (true) {
	    int cnt = ((int) data [pos++]) & 0x0ff;
	    //System.out.println ("extension size: "+cnt);
	    if (cnt <= 0) break;
	    pos += cnt;
	}
    }



    int readWord () {
 	int w = (((int) data [pos]) & 0x0ff)
	    | ((((int) data [pos+1]) & 0x0ff) << 8);
 	pos += 2;
	return w;// b | (data [pos++] << 8);
     }


    void initTable () {
 	codeSize = minCodeSize + 1;
 	clearCode = 1 << minCodeSize;
 	eofCode = clearCode + 1;
 	firstFree = clearCode + 2;
 	freeCode = firstFree;
 	maxCode = 1 << codeSize;

 	mask = (1 << codeSize) - 1;
    }

    void expandData () {

 	minCodeSize = ((int) data [pos++]) & 0x0ff;

 	initTable ();

 	prefix = new short [maxCode - firstFree];
 	suffix = new byte [maxCode - firstFree];
 	stack = new byte [maxCode - firstFree + 1];


 	int oldCode = 0;
 	int suffixChar;
 	int finalChar = 0;
 	int inputCode;
 	int sp = 0;

 	while (true) {
 	    int code = readCode ();
 	    if (code == eofCode) break;
 	    if (code == clearCode) {
 		initTable ();
 		code = readCode ();
 		oldCode = code;
 		suffixChar = code;
 		finalChar = code;
 		putByte (suffixChar);
 	    }
 	    else {
 		inputCode = code;
 		if (code >= freeCode) {
 		    code = oldCode;
 		    stack [sp++] = (byte) finalChar;
 		}

 		while (code >= firstFree) {
 		    stack [sp++] = suffix [code - firstFree];
 		    code = prefix [code - firstFree];
 		}

 		finalChar = code;
 		suffixChar = code;
 		stack [sp++] = (byte) finalChar;

 		while (sp > 0)
 		    putByte (stack [--sp]);

 		suffix [freeCode - firstFree] = (byte) suffixChar;
 		prefix [freeCode - firstFree] = (short) oldCode;

 		freeCode++;
 		oldCode = inputCode;

 		if (freeCode >= maxCode)
 		    if (codeSize < 12) {
 			codeSize++;
 			mask = (1 << codeSize) - 1;
 			maxCode <<= 1;

 			if (prefix.length < maxCode) {
 			    short [] tmps = prefix;
 			    prefix = new short [maxCode - firstFree];
 			    System.arraycopy (tmps, 0, prefix, 0, tmps.length);

 			    byte [] tmp = suffix;
 			    suffix = new byte [maxCode - firstFree];
 			    System.arraycopy (tmp, 0, suffix, 0, tmp.length);

 			    tmp = stack;
 			    stack = new byte [maxCode - firstFree + 1];
 			    System.arraycopy (tmp, 0, stack, 0, tmp.length);
 			}
 		    }
 	    }
 	}
    }



    void readImage () {

 	left = readWord ();
 	top = readWord ();
 	width = readWord ();
 	height = readWord ();

 	row = 0;

 	int misc = ((int)data [pos++]) & 0x0ff;

 	interlaced = (misc & 64) != 0;

//#ifdef giftest
//#     System.out.println ("left: "+left + " top: "+top
//# 			+ " width: "+width + " height: "+height
//# 			+ " interlaced: "+interlaced);
//#endif

 	pixelBits = globalPixelBits;
 	if ((misc & 128) != 0) {
 	    pixelBits = (misc & 7) + 1;
//#ifdef giftest 
//#       System.out.println ("Reading local color map");
//#endif
 	    colorMap = new byte [ 1 << pixelBits ];
 	    readColorMap (colorMap, colorMap.length);
 	}
 	else {
//#ifdef giftest
//#      System.out.println ("no local color map");
//#endif
 	    colorMap = globalColorMap;
 	}

 	writePos = 0;
 	colors = 1 << pixelBits;
 	//blockSize = 0;
 	bufPos = 0;
	bufEnd = 0;

 	expandData ();
     }


     void readColorMap (byte [] map, int len) {

 	for (int i = 0; i < len; i++) {
 	    int r = 255-(((int) data [pos++]) &0x0ff);
 	    int g = 255-(((int) data [pos++]) &0x0ff);
 	    int b = 255-(((int) data [pos++]) &0x0ff);

 	    if (screenBits < 8) {
 		map [i] = (byte) (((r + g + b) / 3)
 				  >> (8-screenBits));
 	    }
 	    else {
 		r /= 43;
 		g /= 43;
 		b /= 43;
 		map [i] = (byte) (g
 				  + (6 * (b % 3))
 				  + r * 3 * 6
 				  + (b / 3) * 3 * 6 * 6);
 	    }


       //	    System.out.println ("r="+r+" g="+g+" b="+b + " map="+map[i]);


 	}
     }

    /*void putByte (int code) {
      writeBuf [writePos] = colorMap [code & 0x0ff];
      if (++writePos >= width) {
      
      
      //System.out.println("interlaced: " + interlaced + " " + width);
      image.setPixels (left, top + row, width, 1, null, writeBuf, 0, width, trans);
      


      writePos = 0;
      if (interlaced) {
      row += passInc[pass];
      if (row >= height) row = starts[++pass];
      } else {
      row++;
      }
      }
      }*/



    void putByte (int code) {
      writeBuf [writePos] = colorMap [code & 0x0ff];
      if (++writePos >= width) {
      
	    //#ifdef giftest
	    //#       for (int i = 0; i < width; i++)
	    //# 	System.out.print ((char) (writeBuf [i]+65));
	    //#       System.out.println ();
	    //# 
	    //#else
	    
	    int dy = top + (interlaced && row + 13 <= height
	    ? (13 * (row / 13)) + interlaceMap [row % 13]
	    : row);
	    
 	    
	    image.setPixels (left, dy, width, 1, null, writeBuf, 0, width);
	    //#endif
	    
 	    
	    writePos = 0;
 	    
	    row++;
	    }
     
	    }
    

     int readCode () {

 	while (filled < codeSize) {

 	    if (bufPos >= bufEnd) {
 		int blockSize = ((int) data [pos++]) & 0x0ff;
		bufPos = pos;
		pos += blockSize;
		bufEnd = pos;
 	    }

 	    current |= (((int) data [bufPos++]) & 255) << filled;
 	    filled += 8;
 	}

 	int result = current & mask;

 	filled -= codeSize;
 	current = current >> codeSize;

 	//	System.out.println ("rc: "+(current & mask));
 	return result;
     }

    

//#ifdef giftest
//# 
//#    public static void main (String [] argv) throws IOException {
//# 
//# 	new GifLoader (new byte [] {
//# 	71,73,70,56,55,97,57,0,88,0,-128,0,0,0,0,0,-1,-1,-1,44,
//# 	0,0,0,0,57,0,88,0,0,2, -2,-116,-113,-87,-53,-19,15,-93,-100,-76,
//# 	-38,-117,-77,-34,-68,-5,10,124,98,16,-114,94,105,114,104,-86,-83,108,-26,-66,
//# 	86,44,83,0,93,75,120,14,-19,-4, -29,-5,49,110,66,80,-79,-121,8,-54,
//# 	92,68,-125,114,-103,88,61,89,-51,-125,-12,-104,-116,-94,-90,-44,-88,21,-101,13,
//# 	-109,-72,-87,-19,22,44,110,86,-47,103,39,-71,28,82,-65,77,68,57,58,25,
//# 	-49,-33,-83,-7,91,-48,-65,-90,2,56,-24,-44,48,8,-40,-126,-24,70,72,115,
//# 	120,-120,-31,-120,88,23,73,73,56,83,-119,105,-23,54,-26,119,-111,25,-55,-121,
//# 	23,-57,-23,-87,41,-58,103,57,89,8,-7,41,-91,-72,-86,-70,-15,8,-94,71,
//# 	34,-37,-87,-29,58,-74,121,-37,-118,-69,57,-37,-21,-5,26,44,-20,-85,-48,-9,
//# 	97,-70,-16,-70,72,55,-116,26,-88,60,-4,-69,68,-3,50,11,-3,92,29,77,
//# 	-23,32,29,-47,44,2,-114,-60,89,44,-118,107,70,28,78,-35,-50,-60,-27,-34,
//# 	-38,-51,26,-97,121,82,47,62,98,-65,-57,-33,63,116,-66,103,89,63,110,-2,
//# 	8,-18,50,-73,70,-45,28,100,-32,-86,44,83,100,-48,-122,52,98,19,93,45,
//# 	20,-59,48,97,12,94,-121,17,39,16,-84,-27,44,-44,60,24,-127,38,-87,-61,
//# 	19,-86,35,-69,119,-92,80,-15,-30,69,46,-41,-69,102,-23,44,-26,-13,-88,-79,
//# 	-104,26,116,55,101,-94,12,-71,72,-29,-49,82,102,-76,-16,28,-38,-14,82,81,
//# 	-116,46,69,54,85,10,52,106,-75,-118,11,59,41,84,56,18,-31,28,-107,108,
//# 	98,98,41,-31,-11,-120,-64,-128,92,-3,-103,61,-117,54,-83,-38,-75,17,10,0, 0,59}).load ();
//# 
//# 	System.out.println ("Ready!");
//# 	System.exit (0);
//#    }
//# 
//#endif



}

/*
 CVS $Log: GifLoader.java,v $
 CVS Revision 1.6  2001/05/12 00:20:46  mkroll
 CVS Changes for HE330.
 CVS
 */

