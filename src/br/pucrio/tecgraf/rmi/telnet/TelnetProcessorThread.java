package br.pucrio.tecgraf.rmi.telnet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiServerException;
import br.pucrio.tecgraf.rmi.socket.AbstractProcessorThread;
import br.pucrio.tecgraf.rmi.util.json.JsonOutputStream;

/**
 * 
 * 
 * @author Tecgraf
 */
public class TelnetProcessorThread extends AbstractProcessorThread {

  /**
   * @param server
   * @param socket
   * @throws SocketException
   * @throws NotBoundException
   * @throws RemoteException
   * @throws AccessException
   */
  public TelnetProcessorThread(IRmiServer server, Socket socket)
    throws SocketException, AccessException, RemoteException, NotBoundException {
    super(server, socket);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void perform() throws SocketException, IOException {
    Socket socket = this.getSocket();
    OutputStream output = socket.getOutputStream();
    LineNumberReader reader =
      new LineNumberReader(new InputStreamReader(socket.getInputStream(),
        "utf-8"));
    while (this.isRunning() && !socket.isClosed()) {
      Object object = null;
      Throwable error = null;
      try {
        String line = reader.readLine();
        if (line.trim().length() == 0) {
          output.write('\n');
          continue;
        }
        object = this.process(line);
      }
      catch (RmiServerException e) {
        error = e.getCause();
      }
      catch (Throwable e) {
        error = e;
      }
      if (!socket.isClosed()) {
        JsonOutputStream stream = new JsonOutputStream(output);
        if (error != null) {
          output.write("throw ".getBytes());
          stream.writeObject(error);
        }
        else {
          output.write("return ".getBytes());
          stream.writeObject(object);
        }
        output.write('\n');
      }
    }
  }

  /**
   * @param line
   * @return resposta
   * @throws Throwable
   */
  private Object process(String line) throws Throwable {
    boolean isString = false;
    StringBuilder sb = new StringBuilder();
    List<String> args = new ArrayList<String>();
    int size = line.length();
    for (int n = 0; n < size; n++) {
      char c = line.charAt(n);
      if (c <= ' ') {
        String arg = sb.length() == 0 ? "" : sb.toString().trim();
        if (arg.length() > 0) {
          args.add(arg);
          sb.delete(0, sb.length());
        }
      }
      else if (c == '\"') {
        if (!isString) {
          isString = true;
        }
        else {
          args.add(sb.toString());
          sb.delete(0, sb.length());
          isString = false;
        }
      }
      else {
        sb.append(c);
      }
    }
    {
      String arg = sb.length() == 0 ? "" : sb.toString().trim();
      if (arg.length() > 0) {
        args.add(arg);
        sb.delete(0, sb.length());
      }
    }
    return this.process(args);
  }

  /**
   * @param args
   * @return resposta
   * @throws Throwable
   */
  private Object process(List<String> args) throws Throwable {
    String cmd = args.get(0);
    if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("exit")) {
      try {
        this.getSocket().close();
      }
      catch (IOException e) {
      }
      this.quit();
      return null;
    }
    else {
      return TelnetCommand.execute(this.getServer(), this.getContext(), args);
    }
  }

}
