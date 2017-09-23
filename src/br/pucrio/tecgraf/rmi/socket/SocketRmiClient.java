package br.pucrio.tecgraf.rmi.socket;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.AbstractRmiClient;
import br.pucrio.tecgraf.rmi.util.StringUtil;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class SocketRmiClient extends AbstractRmiClient {

  /** Socket */
  private Socket socket;

  /**
   * @param port
   * @throws IOException
   */
  public SocketRmiClient(int port) throws IOException {
    this.socket = new Socket("localhost", port);
  }

  /**
   * @param host
   * @throws IOException
   */
  public SocketRmiClient(String host) throws IOException {
    this.socket = new Socket("localhost", 80);
  }

  /**
   * @param host
   * @param port
   * @throws IOException
   */
  public SocketRmiClient(String host, int port) throws IOException {
    this.socket = new Socket(host, port);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Remote> E lookup(String name, Class<E> c)
    throws RemoteException, NotBoundException, AccessException {
    try {
      StringUtil.writeLine(socket.getOutputStream(), "lookup " + name);
      String response = StringUtil.readLine(socket.getInputStream());
      if (response.startsWith("return ")) {
        response = response.substring("return ".length());
        response = response.substring(1, response.length() - 1);
        @SuppressWarnings("unchecked")
        E proxy =
          (E) Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c },
            new SocketRemoteStub(socket, response));
        return proxy;
      }
      else {
        throw new NotBoundException(response);
      }
    }
    catch (IOException e) {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] list() throws RemoteException, AccessException {
    try {
      StringUtil.writeLine(socket.getOutputStream(), "list");
      String response = StringUtil.readLine(socket.getInputStream());
      return response.split(", ");
    }
    catch (IOException e) {
      throw new RemoteException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    this.socket.close();
  }

}
