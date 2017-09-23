package br.pucrio.tecgraf.rmi.telnet;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiServerException;
import br.pucrio.tecgraf.rmi.RmiUserContext;
import br.pucrio.tecgraf.rmi.binary.data.CallBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.ListBinaryCommand;
import br.pucrio.tecgraf.rmi.binary.data.LookupBinaryCommand;

/**
 * Executa os comandos de Telnet
 * 
 * @author Tecgraf
 */
public class TelnetCommand {

  /**
   * @param server
   * @param context
   * @param args
   * @return resposta
   * @throws Throwable
   */
  public static Object execute(IRmiServer server, RmiUserContext context,
    List<String> args) throws Throwable {
    String cmd = args.get(0);
    if (cmd.equalsIgnoreCase("lookup")) {
      return lookup(server, context, args);
    }
    else if (cmd.equalsIgnoreCase("list")) {
      return list(server, context, args);
    }
    else if (cmd.equalsIgnoreCase("header")) {
      return header(server, context, args);
    }
    else if (cmd.equalsIgnoreCase("call")) {
      return call(server, context, args);
    }
    else {
      throw new RemoteException(cmd + ": command not found");
    }
  }

  /**
   * @param server
   * @param context
   * @param args
   * @return contexto
   */
  private static Object header(IRmiServer server, RmiUserContext context,
    List<String> args) {
    return context;
  }

  /**
   * @param server
   * @param context
   * @param args
   * @return busca por um serviço
   * @throws Throwable
   */
  private static Serializable lookup(IRmiServer server, RmiUserContext context,
    List<String> args) throws Throwable {
    if (args.size() != 2) {
      throw new NotBoundException("lookup <service_name>");
    }
    return new LookupBinaryCommand(args.get(1)).execute(server, context);
  }

  /**
   * @param server
   * @param context
   * @param args
   * @return lista de serviços
   * @throws RemoteException
   * @throws AccessException
   * @throws NotBoundException
   * @throws RmiServerException
   */
  private static Object list(IRmiServer server, RmiUserContext context,
    List<String> args) throws AccessException, RemoteException,
    NotBoundException, RmiServerException {
    ListBinaryCommand cmd;
    if (args.size() == 1) {
      cmd = new ListBinaryCommand();
    }
    else if (args.size() == 2) {
      cmd = new ListBinaryCommand(args.get(1));
    }
    else {
      return "list (<service_name>)?";
    }
    try {
      return cmd.execute(server, context);
    }
    catch (Throwable e) {
      throw new RmiServerException(e);
    }
  }

  /**
   * @param server
   * @param context
   * @param args
   * @return lista de serviços
   * @throws Throwable
   */
  private static Object call(IRmiServer server, RmiUserContext context,
    List<String> args) throws Throwable {
    if (args.size() < 3) {
      throw new IllegalArgumentException(
        "call <service_name> <method_name> (<argument>)*");
    }
    return new CallBinaryCommand(args.get(1), args.get(2), args.subList(3,
      args.size()).toArray()).execute(server, context);
  }

}
