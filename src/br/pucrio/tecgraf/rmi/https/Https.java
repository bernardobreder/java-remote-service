package br.pucrio.tecgraf.rmi.https;

import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.security.Provider;
import java.security.Security;

public class Https {

  /**
   * @throws Exception
   */
  public static void init() throws Exception {
    String strVendor = System.getProperty("java.vendor");
    String strVersion = System.getProperty("java.version");
    //Assumes a system version string of the form:
    //[major].[minor].[release]  (eg. 1.2.2)
    Double dVersion = new Double(strVersion.substring(0, 3));
    //If we are running in a MS environment, use the MS stream handler.
    if (strVendor.indexOf("Microsoft") >= 0) {
      try {
        Class<?> clsFactory =
          Class.forName("com.ms.net.wininet.WininetStreamHandlerFactory");
        if (null != clsFactory) {
          URL.setURLStreamHandlerFactory((URLStreamHandlerFactory) clsFactory
            .newInstance());
        }
      }
      catch (ClassNotFoundException cfe) {
        throw new Exception("Unable to load the Microsoft SSL "
          + "stream handler.  Check classpath." + cfe.toString());
      }
      //If the stream handler factory has 
      //already been successfully set
      //make sure our flag is set and eat the error
      catch (Error err) {
        //        m_bStreamHandlerSet = true;
      }
    }
    //If we are in a normal Java environment,
    //try to use the JSSE handler.
    //NOTE:  JSSE requires 1.2 or better
    else if (1.2 <= dVersion.doubleValue()) {
      System.setProperty("java.protocol.handler.pkgs",
        "com.sun.net.ssl.internal.www.protocol");
      try {
        //if we have the JSSE provider available, 
        //and it has not already been
        //set, add it as a new provide to the Security class.
        Class<?> clsFactory =
          Class.forName("com.sun.net.ssl.internal.ssl.Provider");
        if ((null != clsFactory) && (null == Security.getProvider("SunJSSE"))) {
          Security.addProvider((Provider) clsFactory.newInstance());
        }
      }
      catch (ClassNotFoundException cfe) {
        throw new Exception("Unable to load the JSSE SSL stream handler."
          + "Check classpath." + cfe.toString());
      }
    }
  }

}
