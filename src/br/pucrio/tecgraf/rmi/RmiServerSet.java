package br.pucrio.tecgraf.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Conjunto de servidores
 * 
 * @author Tecgraf
 */
public class RmiServerSet extends AbstractRmiServer {

  /** Servidores */
  private final List<IRmiServer> servers = new ArrayList<IRmiServer>();
  /** Prefixo */
  private String defaultPrefix = "jdk";

  //  /**
  //   * {@inheritDoc}
  //   */
  //  @Override
  //  public Remote lookup(String name) throws RemoteException, NotBoundException,
  //    AccessException {
  //    String defaultPrefix = this.getDefaultPrefix();
  //    int prefixIndex = name.indexOf(':');
  //    if (prefixIndex < 0) {
  //      name = defaultPrefix + ":" + name;
  //      prefixIndex = defaultPrefix.length();
  //    }
  //    for (IRmiServer server : this.servers) {
  //      if (name.startsWith(server.getPrefixName())) {
  //        name = name.substring(defaultPrefix.length() + 1);
  //        return server.lookup(name);
  //      }
  //    }
  //    return null;
  //  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind(String name, Remote obj) throws RemoteException,
    AlreadyBoundException, AccessException {
    this.checkRemoteObject(obj);
    try {
      for (IRmiServer server : this.servers) {
        server.bind(name, obj);
      }
    }
    catch (Exception e) {
      for (IRmiServer server : this.servers) {
        try {
          server.unbind(name);
        }
        catch (NotBoundException e1) {
        }
      }
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind(String name) throws RemoteException, NotBoundException,
    AccessException {
    for (IRmiServer server : this.servers) {
      server.unbind(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rebind(String name, Remote obj) throws RemoteException,
    AccessException {
    this.checkRemoteObject(obj);
    for (IRmiServer server : this.servers) {
      server.rebind(name, obj);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] list() throws RemoteException, AccessException {
    String defaultPrefix = this.getDefaultPrefix();
    for (IRmiServer server : this.servers) {
      if (server.getPrefixName().equals(defaultPrefix)) {
        return server.list();
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPrefixName() {
    return "all";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() throws RemoteException {
    for (IRmiServer server : this.servers) {
      server.stop();
    }
  }

  /**
   * @param server
   */
  public void addRmiServer(IRmiServer server) {
    this.servers.add(server);
  }

  /**
   * @param server
   */
  public void removeRmiServer(IRmiServer server) {
    this.servers.remove(server);
  }

  /**
   * 
   */
  public void clearRmiServer() {
    this.servers.clear();
  }

  /**
   * Retorna
   * 
   * @return defaultPrefix
   */
  public String getDefaultPrefix() {
    return defaultPrefix;
  }

  /**
   * @param defaultPrefix
   */
  public void setDefaultPrefix(String defaultPrefix) {
    this.defaultPrefix = defaultPrefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getDefaultPrefix();
  }

}
