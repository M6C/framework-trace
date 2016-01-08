package framework.ressource.util;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class UtilTrace {

  private UtilTrace() {
  }

  public static String formatTrace(Exception ex) {
    return formatTrace(ex, true);
  }

  public static String formatTrace(Exception ex, boolean date) {
    StackTraceElement[] stackTrace = ex.getStackTrace();
    StringBuffer ret = new StringBuffer();
    String[][] tabMsg = new String[4][stackTrace.length];
    int iNumMax=0, iFilMax=0, iClsMax=0;
    for( int i=0 ; i<stackTrace.length ; i++)
    {
      tabMsg[0][i] = Integer.toString(stackTrace[i].getLineNumber());
      tabMsg[1][i] = stackTrace[i].getFileName();
      tabMsg[2][i] = stackTrace[i].getClassName();
      tabMsg[3][i] = stackTrace[i].getMethodName();
      iNumMax = ((tabMsg[0][i]!=null) && tabMsg[0][i].length()>iNumMax) ? tabMsg[0][i].length() : iNumMax;
      iFilMax = ((tabMsg[1][i]!=null) && tabMsg[1][i].length()>iFilMax) ? tabMsg[1][i].length() : iFilMax;
      iClsMax = ((tabMsg[2][i]!=null) && tabMsg[2][i].length()>iClsMax) ? tabMsg[2][i].length() : iClsMax;
    }
    if (date)
      ret.append(" -> ").append(new Date().toString()).append("\r\n");
    for( int i=0 ; i<stackTrace.length ; i++) {
      buildTraceLog(ret, "LINE:", tabMsg[0][i], " -", iNumMax);
      buildTraceLog(ret, "FILE:[", tabMsg[1][i], "]", iFilMax);
      buildTraceLog(ret, "CLASS:{", tabMsg[2][i], "}", iClsMax);
      buildTraceLog(ret, "METHODE:\"", tabMsg[3][i], "\"", 0);
      ret.append("\r\n");
    }
    return ret.toString();
  }
  protected static void buildTraceLog(StringBuffer stb, String head, String msg, String foot, int iMaxLen) {
    stb.append(head).append(msg).append(foot);
    if (msg!=null) {
      for (int i = msg.length(); i <= iMaxLen; i++) {
        stb.append(" ");
      }
    }
  }
}
