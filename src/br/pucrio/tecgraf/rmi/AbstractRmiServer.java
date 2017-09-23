package br.pucrio.tecgraf.rmi;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * 
 * @author Tecgraf
 */
public abstract class AbstractRmiServer implements IRmiServer {

  /**
   * {@inheritDoc}
   */
  @Override
  public Remote lookup(String name) throws RemoteException, NotBoundException,
    AccessException {
    throw new IllegalStateException();
  }

  /**
   * @param obj
   */
  protected void checkRemoteObject(Remote obj) {
    if (obj instanceof Serializable == false) {
      throw new IllegalArgumentException(
        "remote object is not java.io.Serializable");
    }
    if (obj instanceof Remote == false) {
      throw new IllegalArgumentException("remote object is not java.rmi.Remote");
    }
    Class<?> clazz = obj.getClass();
    while (clazz != Object.class) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        boolean found = false;
        for (Class<?> c : method.getExceptionTypes()) {
          if (c == RemoteException.class) {
            found = true;
            break;
          }
        }
        if (!found) {
          throw new IllegalArgumentException("method '" + method.getName()
            + "' not throws java.rmi.RemoteException");
        }
      }
      clazz = clazz.getSuperclass();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getPrefixName();
  }

}
