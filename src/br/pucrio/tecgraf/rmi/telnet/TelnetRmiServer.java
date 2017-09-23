package br.pucrio.tecgraf.rmi.telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Map;

import br.pucrio.tecgraf.rmi.AbstractRmiServer;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class TelnetRmiServer extends AbstractRmiServer {

  /** Porta */
  private int port;
  /** Servidor */
  private ServerSocket server;
  /** Mapa de Objetos Remotos */
  private Map<String, Remote> remotes = new HashMap<String, Remote>();
  /** Mapa de Objetos Remotos */
  private static Map<Integer, TelnetAcceptThread> threads =
    new HashMap<Integer, TelnetAcceptThread>();

  /**
   * @param port
   * @throws IOException
   */
  public TelnetRmiServer(int port) throws IOException {
    this.port = port;
    synchronized (threads) {
      if (threads.containsKey(port)) {
        throw new IllegalArgumentException("port " + port + " already using");
      }
      this.server = new ServerSocket(port);
      TelnetAcceptThread thread = new TelnetAcceptThread(this, this.server);
      threads.put(port, thread);
      thread.start();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Remote lookup(String name) {
    synchronized (remotes) {
      return remotes.get(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(String name, Remote obj) {
    this.checkRemoteObject(obj);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind(String name) {
    synchronized (remotes) {
      remotes.remove(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rebind(String name, Remote obj) {
    this.checkRemoteObject(obj);
    synchronized (remotes) {
      remotes.put(name, obj);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] list() {
    synchronized (remotes) {
      return remotes.keySet().toArray(new String[remotes.size()]);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPrefixName() {
    return "telnet";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    synchronized (threads) {
      TelnetAcceptThread thread = threads.get(port);
      if (thread != null) {
        thread.quit();
      }
    }
  }
}
