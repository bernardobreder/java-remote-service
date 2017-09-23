package br.pucrio.tecgraf.rmi.binary;

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
public class BinaryAcceptThread extends AbstractAcceptThread {

  /**
   * @param server
   * @param serverSocket
   */
  public BinaryAcceptThread(BinaryRmiServer server, ServerSocket serverSocket) {
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
    return new BinaryProcessorThread(this.getServer(), socket);
  }

}
