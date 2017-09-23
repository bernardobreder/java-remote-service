package br.pucrio.tecgraf.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public interface IRmiServer extends Registry {

  /**
   * Retorna o nome do prefix para ser consultado
   * 
   * @return nome do prefixo
   */
  public String getPrefixName();

  /**
   * @throws RemoteException
   */
  public void stop() throws RemoteException;

}
