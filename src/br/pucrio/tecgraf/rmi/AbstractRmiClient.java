package br.pucrio.tecgraf.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * 
 * @author Tecgraf
 */
public abstract class AbstractRmiClient implements IRmiClient {

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(String name, Remote obj) throws RemoteException,
    AlreadyBoundException, AccessException {
    throw new IllegalStateException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind(String name) throws RemoteException, NotBoundException,
    AccessException {
    throw new IllegalStateException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rebind(String name, Remote obj) throws RemoteException,
    AccessException {
    throw new IllegalStateException();
  }

}
