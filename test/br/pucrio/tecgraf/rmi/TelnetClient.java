package br.pucrio.tecgraf.rmi;

import br.pucrio.tecgraf.rmi.telnet.TelnetRmiClient;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class TelnetClient {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    IRmiClient clientRegistry = new TelnetRmiClient(8080);
    LoginService service = clientRegistry.lookup("A", LoginService.class);
    System.out.println(service.login("", ""));
    clientRegistry.close();
  }

}
