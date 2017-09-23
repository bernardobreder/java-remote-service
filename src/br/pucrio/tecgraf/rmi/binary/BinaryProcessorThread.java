package br.pucrio.tecgraf.rmi.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiServerException;
import br.pucrio.tecgraf.rmi.RmiUserContext;
import br.pucrio.tecgraf.rmi.binary.data.AbstractBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.CallBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.HeadBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.ListBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.LookupBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.QuitBinaryCommand;
import br.pucrio.tecgraf.rmi.socket.AbstractProcessorThread;
import br.pucrio.tecgraf.rmi.util.RmiDataInputStream;
import br.pucrio.tecgraf.rmi.util.RmiDataOutputStream;

/**
 * 
 * 
 * @author Tecgraf
 */
public class BinaryProcessorThread extends AbstractProcessorThread {

  /**
   * @param server
   * @param socket
   * @throws SocketException
   * @throws NotBoundException
   * @throws RemoteException
   * @throws AccessException
   */
  public BinaryProcessorThread(IRmiServer server, Socket socket)
    throws SocketException, AccessException, RemoteException, NotBoundException {
    super(server, socket);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void perform() throws SocketException, IOException {
    Socket socket = this.getSocket();
    InputStream input = socket.getInputStream();
    OutputStream output = socket.getOutputStream();
    RmiDataInputStream in = new RmiDataInputStream(input);
    RmiDataOutputStream out = new RmiDataOutputStream(output);
    try {
      while (this.isRunning() && !socket.isClosed()) {
        Object object = null;
        Throwable error = null;
        try {
          object = this.process(in);
        }
        catch (RmiServerException e) {
          error = e.getCause();
        }
        catch (Throwable e) {
          error = e;
        }
        finally {
          in.close();
        }
        if (!socket.isClosed()) {
          if (error != null) {
            out.writeBoolean(false);
            out.writeObject(error);
          }
          else {
            out.writeBoolean(true);
            out.writeObject(object);
          }
          out.writeEof();
        }
      }
    }
    finally {
      in.close();
      out.close();
    }
  }

  /**
   * @param in
   * @return resultado da consulta
   * @throws IOException
   * @throws RmiServerException
   * @throws NotBoundException
   */
  private Serializable process(RmiDataInputStream in) throws IOException,
    NotBoundException, RmiServerException {
    //    RmiUserContextSerializabler rmiUserContextSerializabler =
    //      new RmiUserContextSerializabler();
    //    rmiUserContextSerializabler.read(in);
    //    RmiUserContext context = rmiUserContextSerializabler.getData();
    return this.process(in, this.getContext());
  }

  /**
   * @param in
   * @param context
   * @return resultado da consulta
   * @throws IOException
   * @throws NotBoundException
   * @throws RmiServerException
   */
  private Serializable process(RmiDataInputStream in, RmiUserContext context)
    throws IOException, NotBoundException, RmiServerException {
    int opcode = in.readInt();
    AbstractBinaryCommand command;
    switch (opcode) {
      case AbstractBinaryCommand.QUIT_OPCODE:
        command = new QuitBinaryCommand();
        break;
      case AbstractBinaryCommand.HEADER_OPCODE:
        command = new HeadBinaryCommand();
        break;
      case AbstractBinaryCommand.LOOKUP_OPCODE:
        command = new LookupBinaryCommand();
        break;
      case AbstractBinaryCommand.LIST_OPCODE:
        command = new ListBinaryCommand();
        break;
      case AbstractBinaryCommand.CALL_OPCODE:
        command = new CallBinaryCommand();
        break;
      default:
        throw new NotBoundException("opcode " + opcode + " is unknown");
    }
    command.read(in);
    in.skipToEof();
    return this.process(in, context, command);
  }

  /**
   * @param in
   * @param context
   * @param command
   * @return resposta
   * @throws RmiServerException
   */
  private Serializable process(RmiDataInputStream in, RmiUserContext context,
    AbstractBinaryCommand command) throws RmiServerException {
    Throwable t = null;
    Serializable result = null;
    try {
      result = command.execute(this.getServer(), context);
    }
    catch (Throwable e) {
      t = e;
    }
    if (command instanceof QuitBinaryCommand) {
      try {
        this.getSocket().close();
      }
      catch (IOException e) {
      }
      this.quit();
    }
    if (t != null) {
      throw new RmiServerException(t);
    }
    else {
      return result;
    }
  }

}
