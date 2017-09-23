package br.pucrio.tecgraf.rmi;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public interface IRmiClient {

  /**
   * @param name
   * @param c
   * @return stub do serviço
   * @throws RemoteException
   * @throws NotBoundException
   * @throws AccessException
   */
  public <E extends Remote> E lookup(String name, Class<E> c)
    throws RemoteException, NotBoundException, AccessException;

  /**
   * @return nome de todos os serviços
   * @throws RemoteException
   * @throws AccessException
   */
  public String[] list() throws RemoteException, AccessException;

  /**
   * Realiza a associação de um serviço a uma implementação
   * 
   * @param name
   * @param obj
   * @throws RemoteException
   * @throws AlreadyBoundException
   * @throws AccessException
   */
  public void bind(String name, Remote obj) throws RemoteException,
    AlreadyBoundException, AccessException;

  /**
   * Desassocia um serviço
   * 
   * @param name
   * @throws RemoteException
   * @throws NotBoundException
   * @throws AccessException
   */
  public void unbind(String name) throws RemoteException, NotBoundException,
    AccessException;

  /**
   * Reassocia um serviço a uma implementação
   * 
   * @param name
   * @param obj
   * @throws RemoteException
   * @throws AccessException
   */
  public void rebind(String name, Remote obj) throws RemoteException,
    AccessException;

  /**
   * @throws IOException
   */
  public void close() throws IOException;

}
