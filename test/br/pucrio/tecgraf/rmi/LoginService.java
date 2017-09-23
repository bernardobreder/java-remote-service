package br.pucrio.tecgraf.rmi;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * 
 * @author Tecgraf
 */
public interface LoginService extends Remote, Serializable {

  /**
   * @param username
   * @param password
   * @return chave
   * @throws RemoteException
   */
  public String login(String username, String password) throws RemoteException;

}
