package br.pucrio.tecgraf.rmi;

import br.pucrio.tecgraf.rmi.binary.BinaryRmiClient;
import br.pucrio.tecgraf.rmi.jdk.JdkRmiClient;
import br.pucrio.tecgraf.rmi.telnet.TelnetRmiClient;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class RmiClient {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    {
      IRmiClient clientRegistry = new JdkRmiClient(9090);
      LoginService service = clientRegistry.lookup("A", LoginService.class);
      service.login("", "");
    }
    {
      IRmiClient clientRegistry = new TelnetRmiClient(8080);
      LoginService service = clientRegistry.lookup("A", LoginService.class);
      service.login("", "");
    }
    {
      IRmiClient clientRegistry = new BinaryRmiClient(5000);
      LoginService service = clientRegistry.lookup("A", LoginService.class);
      service.login("", "");
    }

  }

}
