package br.pucrio.tecgraf.rmi.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.pucrio.tecgraf.rmi.IRmiServer;

/**
 * Thread do Accept do Telnet
 * 
 * @author Tecgraf
 */
public abstract class AbstractAcceptThread extends Thread {

  /** Running */
  private boolean running = true;
  /** Servidor */
  private IRmiServer server;
  /** Server Socket */
  private ServerSocket serverSocket;
  /** Mapa de Objetos Remotos */
  private List<AbstractProcessorThread> threads =
    new ArrayList<AbstractProcessorThread>();

  /**
   * @param server
   * @param serverSocket
   */
  public AbstractAcceptThread(IRmiServer server, ServerSocket serverSocket) {
    super(server.getPrefixName().toUpperCase() + " Accept "
      + serverSocket.getLocalPort());
    this.server = server;
    this.serverSocket = serverSocket;
  }

  /**
   * @param socket
   * @return processador
   * @throws AccessException
   * @throws SocketException
   * @throws RemoteException
   * @throws NotBoundException
   */
  public abstract AbstractProcessorThread createProcessorThread(Socket socket)
    throws AccessException, SocketException, RemoteException, NotBoundException;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    while (running) {
      try {
        Socket socket = this.serverSocket.accept();
        AbstractProcessorThread thread = this.createProcessorThread(socket);
        synchronized (threads) {
          this.threads.add(thread);
        }
        thread.start();
      }
      catch (Throwable e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    super.start();
    this.running = true;
  }

  /**
   * Fechar
   */
  public void quit() {
    synchronized (threads) {
      for (AbstractProcessorThread thread : threads) {
        thread.quit();
      }
    }
    this.running = false;
  }

  /**
   * @return servidor
   */
  public IRmiServer getServer() {
    return server;
  }

}
