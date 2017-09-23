package br.pucrio.tecgraf.rmi;

import br.pucrio.tecgraf.rmi.binary.BinaryRmiServer;
import br.pucrio.tecgraf.rmi.jdk.JdkRmiServer;
import br.pucrio.tecgraf.rmi.telnet.TelnetRmiServer;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class Server {

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    RmiServerSet rmiServerSet = new RmiServerSet();
    IRmiServer jdkServerRegistry = new JdkRmiServer(9090);
    IRmiServer telnetServerRegistry = new TelnetRmiServer(8080);
    IRmiServer binaryServerRegistry = new BinaryRmiServer(5000);
    rmiServerSet.addRmiServer(jdkServerRegistry);
    rmiServerSet.addRmiServer(telnetServerRegistry);
    rmiServerSet.addRmiServer(binaryServerRegistry);
    rmiServerSet.rebind("A", new LoginDbService());
  }
}
