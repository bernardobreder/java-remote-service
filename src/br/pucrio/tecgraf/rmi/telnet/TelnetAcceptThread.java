package br.pucrio.tecgraf.rmi.telnet;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.socket.AbstractAcceptThread;
import br.pucrio.tecgraf.rmi.socket.AbstractProcessorThread;

/**
 * Thread do Accept do Telnet
 * 
 * @author Tecgraf
 */
public class TelnetAcceptThread extends AbstractAcceptThread {

  /**
   * @param server
   * @param serverSocket
   */
  public TelnetAcceptThread(TelnetRmiServer server, ServerSocket serverSocket) {
    super(server, serverSocket);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws NotBoundException
   * @throws RemoteException
   * @throws SocketException
   * @throws AccessException
   */
  @Override
  public AbstractProcessorThread createProcessorThread(Socket socket)
    throws AccessException, SocketException, RemoteException, NotBoundException {
    return new TelnetProcessorThread(this.getServer(), socket);
  }

}
