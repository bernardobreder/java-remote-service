package br.pucrio.tecgraf.rmi.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.binary.data.CallBinaryCommand;
import br.pucrio.tecgraf.rmi.util.RmiDataInputStream;
import br.pucrio.tecgraf.rmi.util.RmiDataOutputStream;

/**
 * Stub de Telnet
 * 
 * @author Tecgraf
 */
public class BinaryRemoteStub implements InvocationHandler, Remote,
  Serializable {

  /** Socket */
  private Socket socket;
  /** Name */
  private String name;

  /**
   * @param socket
   * @param name
   */
  public BinaryRemoteStub(Socket socket, String name) {
    this.socket = socket;
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable {
    InputStream inputStream = socket.getInputStream();
    OutputStream outputStream = socket.getOutputStream();
    RmiDataInputStream in = new RmiDataInputStream(inputStream);
    RmiDataOutputStream out = new RmiDataOutputStream(outputStream);
    try {
      CallBinaryCommand command =
        new CallBinaryCommand(name, method.getName(), args);
      command.write(out);
      out.writeEof();
      boolean hasResult = inputStream.read() != 0;
      Object value = in.readObject();
      if (hasResult) {
        return value;
      }
      else {
        throw (Throwable) value;
      }
    }
    catch (ClassNotFoundException e) {
      throw new RemoteException(e.getMessage(), e);
    }
    catch (IOException e) {
      throw new RemoteException(e.getMessage(), e);
    }
    finally {
      in.readEof();
      in.close();
    }
  }

}
