package br.pucrio.tecgraf.rmi.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;
import br.pucrio.tecgraf.rmi.util.StringUtil;

/**
 * 
 * 
 * @author Tecgraf
 */
public abstract class AbstractProcessorThread extends Thread {

  /** Header */
  private final RmiUserContext context = new RmiUserContext();
  /** Server */
  private final IRmiServer server;
  /** Socket */
  private final Socket socket;
  /** Indica se está rodando */
  private boolean running;

  /**
   * @param server
   * @param socket
   * @throws SocketException
   * @throws NotBoundException
   * @throws RemoteException
   * @throws AccessException
   */
  public AbstractProcessorThread(IRmiServer server, Socket socket)
    throws SocketException, AccessException, RemoteException, NotBoundException {
    super(server.getPrefixName().toUpperCase() + " Processor "
      + socket.getLocalPort());
    this.server = server;
    this.socket = socket;
    this.context.set("session", StringUtil.createId(socket));
    for (String service : server.list()) {
      this.context.set(service, server.lookup(service));
    }
  }

  /**
   * @throws SocketException
   * @throws IOException
   * 
   */
  public abstract void perform() throws SocketException, IOException;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    RmiUserContext.set(context);
    try {
      try {
        this.perform();
      }
      catch (SocketException e) {
        if (e.getMessage().trim().equalsIgnoreCase("Broken pipe")) {
        }
        else {
          throw e;
        }
      }
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    finally {
      RmiUserContext.remove();
    }
  }

  /**
   * @return running
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Finaliza o processador
   */
  public void quit() {
    this.running = false;
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
   * @return context
   */
  public RmiUserContext getContext() {
    return context;
  }

  /**
   * @return socket
   */
  public Socket getSocket() {
    return socket;
  }

  /**
   * @return server
   */
  public IRmiServer getServer() {
    return server;
  }

}
