package br.pucrio.tecgraf.rmi.telnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.Remote;

import br.pucrio.tecgraf.rmi.util.json.JsonInputStream;

/**
 * Stub de Telnet
 * 
 * @author Tecgraf
 */
public class TelnetRemoteStub implements InvocationHandler, Remote,
  Serializable {

  /** Socket */
  private Socket socket;
  /** Name */
  private String name;

  /**
   * @param socket
   * @param name
   */
  public TelnetRemoteStub(Socket socket, String name) {
    this.socket = socket;
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable {
    {
      StringBuilder sb = new StringBuilder();
      sb.append("call ");
      sb.append(name);
      sb.append(' ');
      sb.append(method.getName());
      for (Object arg : args) {
        sb.append(' ');
        sb.append('\"');
        sb.append(toString(arg));
        sb.append('\"');
      }
      sb.append('\n');
      socket.getOutputStream().write(sb.toString().getBytes("utf-8"));
    }
    {
      InputStream input = socket.getInputStream();
      ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
      for (int n; (n = input.read()) != -1;) {
        if (n == '\n') {
          break;
        }
        output.write(n);
      }
      String response = new String(output.toByteArray());
      if (response.startsWith("return ")) {
        response = response.substring("return ".length());
        JsonInputStream jsonInput =
          new JsonInputStream(new ByteArrayInputStream(response
            .getBytes("utf-8")));
        return jsonInput.readObject();
      }
      else {
        response = response.substring("throw ".length());
        JsonInputStream jsonInput =
          new JsonInputStream(new ByteArrayInputStream(response
            .getBytes("utf-8")));
        throw (Throwable) jsonInput.readObject();
      }
    }
  }

  /**
   * @param arg
   * @return representação de string
   */
  private Object toString(Object arg) {
    return arg.toString();
  }

}
