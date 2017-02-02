package framework.trace;

import framework.ressource.util.UtilTrace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Trace {

  public Trace() {
  }
  public static void OUT( Object parent, Object obj )
  {
    Trace_OUT( getTrace(parent,obj) );
  }
  public static void OUT( Object obj )
  {
    Trace_OUT( getTrace(obj) );
  }
  public static void ERROR( Object parent, Object obj )
  {
    Trace_ERROR( getTrace(parent,obj) );
  }
  public static void ERROR( Object obj )
  {
    Trace_ERROR( getTrace(obj) );
  }
  public static void DEBUG( Object parent, Object obj )
  {
    Trace_DEBUG( getTrace(parent,obj) );
  }
  public static void DEBUG( Object obj )
  {
    Trace_DEBUG( getTrace(obj) );
  }
  public static void WARNING( Object obj )
  {
    Trace_WARNING( getTrace(obj) );
  }
  public static void WARNING( Object parent, Object obj )
  {
    Trace_WARNING( getTrace(parent, obj) );
  }
  protected static void Trace_OUT(String str) {
    if (str.endsWith("\r\n")) System.out.print(str);
    else                      System.out.println(str);
  }
  protected static void Trace_ERROR(String str) {
    if (str.endsWith("\r\n")) System.err.print(str);
    else                      System.err.println(str);
  }
  protected static void Trace_WARNING(String str) {
    if (str.endsWith("\r\n")) System.out.print(str);
    else                      System.out.println(str);
  }
  protected static void Trace_DEBUG(String str) {
    if (str.endsWith("\r\n")) System.out.print(str);
    else                      System.out.println(str);
  }
  private static String getTrace( Object obj )
  {
    String msg = null;
    if (obj instanceof Throwable) msg = ((Throwable)obj).getMessage();
    else msg = obj.toString();
    return msg;
  }
  private static String getTrace( Object parent, Object obj )
  {
    StringBuffer str = new StringBuffer();
    String msg = null;
    str.append("CLASS:");
    str.append(parent.getClass().getName());
    str.append(" MSG:");
    if (obj==null)
      msg = "null";
    else {
    	if (obj instanceof Throwable) {
    		msg = ((Throwable)obj).getMessage();
	    	if (obj instanceof Exception)
		      msg += "\r\n"+UtilTrace.formatTrace((Exception)obj);
    	}
	    else
	      msg = obj.toString();
    }
    str.append(msg);
    return str.toString();
  }
}
