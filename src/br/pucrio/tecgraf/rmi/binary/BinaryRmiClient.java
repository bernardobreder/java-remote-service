package br.pucrio.tecgraf.rmi.binary;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.AbstractRmiClient;
import br.pucrio.tecgraf.rmi.binary.data.ListBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.LookupBinaryCommand;
import br.pucrio.tecgraf.rmi.util.ProxyUtil;
import br.pucrio.tecgraf.rmi.util.RmiDataInputStream;
import br.pucrio.tecgraf.rmi.util.RmiDataOutputStream;

/**
 * Classe responsável por criar um Servidor RMI.
 * 
 * @author Tecgraf
 */
public class BinaryRmiClient extends AbstractRmiClient {

  /** Socket */
  private Socket socket;

  /**
   * @param port
   * @throws IOException
   */
  public BinaryRmiClient(int port) throws IOException {
    this.socket = new Socket("localhost", port);
  }

  /**
   * @param host
   * @throws IOException
   */
  public BinaryRmiClient(String host) throws IOException {
    this.socket = new Socket(host, 5000);
  }

  /**
   * @param host
   * @param port
   * @throws IOException
   */
  public BinaryRmiClient(String host, int port) throws IOException {
    this.socket = new Socket(host, port);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Remote> E lookup(String name, Class<E> c)
    throws RemoteException, NotBoundException, AccessException {
    try {
      LookupBinaryCommand command = new LookupBinaryCommand(name);
      RmiDataOutputStream out =
        new RmiDataOutputStream(socket.getOutputStream());
      command.write(out);
      out.writeEof();
      RmiDataInputStream in = new RmiDataInputStream(socket.getInputStream());
      try {
        boolean hasResult = in.readBoolean();
        if (hasResult) {
          Object value = in.readObject();
          if (value != null && value instanceof String) {
            return ProxyUtil.createProxy(c, new BinaryRemoteStub(socket, name));
          }
          else {
            throw new NotBoundException(name);
          }
        }
        else {
          throw in.readRemoteException();
        }
      }
      finally {
        in.readEof();
        in.close();
      }
    }
    catch (ClassNotFoundException e) {
      throw new RemoteException(e.getMessage(), e);
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
      ListBinaryCommand command = new ListBinaryCommand();
      RmiDataOutputStream out =
        new RmiDataOutputStream(socket.getOutputStream());
      command.write(out);
      out.writeEof();
      RmiDataInputStream in = new RmiDataInputStream(socket.getInputStream());
      try {
        boolean hasResult = in.readBoolean();
        if (hasResult) {
          return (String[]) in.readObject();
        }
        else {
          throw in.readRemoteException();
        }
      }
      finally {
        in.readEof();
        in.close();
      }
    }
    catch (ClassNotFoundException e) {
      throw new RemoteException(e.getMessage(), e);
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
