package br.pucrio.tecgraf.rmi.jdk;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import br.pucrio.tecgraf.rmi.AbstractRmiServer;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class JdkRmiServer extends AbstractRmiServer {

  /** Registry */
  private Registry registry;

  /**
   * @param port
   * @throws RemoteException
   */
  public JdkRmiServer(int port) throws RemoteException {
    registry = LocateRegistry.createRegistry(port);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(String name, Remote obj) throws RemoteException,
    AlreadyBoundException, AccessException {
    this.checkRemoteObject(obj);
    this.registry.bind(name, createStub(obj));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind(String name) throws RemoteException, NotBoundException,
    AccessException {
    this.registry.unbind(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rebind(String name, Remote obj) throws RemoteException,
    AccessException {
    this.checkRemoteObject(obj);
    this.registry.rebind(name, createStub(obj));
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
  public String getPrefixName() {
    return "jdk";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() throws RemoteException {
    for (String name : this.registry.list()) {
      try {
        this.registry.unbind(name);
      }
      catch (NotBoundException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @param obj
   * @return stub
   * @throws RemoteException
   */
  protected Remote createStub(Remote obj) throws RemoteException {
    return UnicastRemoteObject.exportObject(obj, 0);
  }

}
