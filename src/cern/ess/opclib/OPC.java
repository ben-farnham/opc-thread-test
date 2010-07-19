/*
 * Created on Jun 8, 2006 by M. J. Clayton
 *
 * Copyright (c) 2006 CERN
 * 
 */

package cern.ess.opclib;

//*************/
public class OPC 
//*************/
{
  public static native void init(String host, String server) throws OPCException;
  public static native String[] getItemNames()  throws OPCException;
  public static native String[] getLocalServerList()  throws OPCException;
  public static native boolean readBoolean(String item) throws OPCException;
  public static native String readString(String item) throws OPCException;
  public static native int readInt(String item) throws OPCException;
  public static native float readFloat(String item) throws OPCException;
  public static native void writeBoolean(String item, boolean val) throws OPCException;
  public static native void writeString(String item, String val) throws OPCException;
  public static native void writeInt(String item, String type, int val) throws OPCException;
  public static native void writeFloat(String item, String type, float val) throws OPCException;

  static 
  {
  	try
  	{
      System.out.println("OPC:Trying to load OPCLib.dll");
      System.out.println("OPC:Path = " + System.getProperty("java.library.path"));
      System.loadLibrary("OPCLib");
      System.out.println("OPC:OPCLib.dll loaded");
  	}
  	catch(UnsatisfiedLinkError ule)
  	{
  		System.err.println("OPC:Failed to to load OPCLib.dll");
  		ule.printStackTrace();
  	}
  }
}
