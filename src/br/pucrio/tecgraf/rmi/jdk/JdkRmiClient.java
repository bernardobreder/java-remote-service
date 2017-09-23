package br.pucrio.tecgraf.rmi.jdk;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.pucrio.tecgraf.rmi.AbstractRmiClient;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class JdkRmiClient extends AbstractRmiClient {

  /** Registry */
  private Registry registry;

  /**
   * @param port
   * @throws RemoteException
   */
  public JdkRmiClient(int port) throws RemoteException {
    this.registry = LocateRegistry.getRegistry(port);
  }

  /**
   * @param host
   * @throws RemoteException
   */
  public JdkRmiClient(String host) throws RemoteException {
    this.registry = LocateRegistry.getRegistry(host);
  }

  /**
   * @param host
   * @param port
   * @throws RemoteException
   */
  public JdkRmiClient(String host, int port) throws RemoteException {
    this.registry = LocateRegistry.getRegistry(host, port);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Remote> E lookup(String name, Class<E> c)
    throws RemoteException, NotBoundException, AccessException {
    @SuppressWarnings("unchecked")
    E result = (E) this.registry.lookup(name);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] list() throws RemoteException, AccessException {
    return this.registry.list();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    String[] list = this.registry.list();
    for (String item : list) {
      try {
        this.registry.unbind(item);
      }
      catch (NotBoundException e) {
      }
    }
  }

}
