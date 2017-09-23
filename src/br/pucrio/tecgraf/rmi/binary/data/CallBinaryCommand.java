package br.pucrio.tecgraf.rmi.binary.data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Comando look
 * 
 * @author Tecgraf
 */
public class CallBinaryCommand extends AbstractBinaryCommand {

  /** Nome do serviço */
  private String serviceName;
  /** Nome do método */
  private String methodName;
  /** Parametros */
  private Object[] parameters;

  /**
   * 
   */
  public CallBinaryCommand() {
    super();
  }

  /**
   * @param serviceName
   * @param methodName
   * @param parameters
   */
  public CallBinaryCommand(String serviceName, String methodName,
    Object[] parameters) {
    super();
    this.serviceName = serviceName;
    this.methodName = methodName;
    this.parameters = parameters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(DataInputStream input) throws IOException {
    this.serviceName = input.readUTF();
    this.methodName = input.readUTF();
    int params = input.readInt();
    this.parameters = new Object[params];
    for (int n = 0; n < params; n++) {
      Object paramObject = readObject(input);
      this.parameters[n] = paramObject;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutputStream output) throws IOException {
    output.writeInt(AbstractBinaryCommand.CALL_OPCODE);
    output.writeUTF(serviceName);
    output.writeUTF(methodName);
    output.writeInt(parameters.length);
    for (int n = 0; n < parameters.length; n++) {
      Object parameter = parameters[n];
      this.writeObject(output, parameter);
    }
  }

  /**
   * @param input
   * @return objeto
   * @throws IOException
   */
  private Object readObject(DataInputStream input) throws IOException {
    ObjectInputStream in = new ObjectInputStream(input);
    try {
      return in.readObject();
    }
    catch (ClassNotFoundException e) {
      throw new IOException(e);
    }
    finally {
      //      in.flush();
    }
  }

  /**
   * @param output
   * @param value
   * @throws IOException
   */
  private void writeObject(DataOutputStream output, Object value)
    throws IOException {
    ObjectOutputStream out = new ObjectOutputStream(output);
    out.writeObject(value);
    //      out.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable execute(IRmiServer server, RmiUserContext context)
    throws Throwable {
    try {
      Object remote = context.getObject(serviceName);
      if (remote instanceof Remote == false) {
        throw new NotBoundException("call <service_name>");
      }
      List<Method> list = new ArrayList<Method>();
      Method[] methods = remote.getClass().getMethods();
      for (Method method : methods) {
        if (method.getName().equals(methodName)) {
          list.add(method);
        }
      }
      for (int n = list.size() - 1; n >= 0; n--) {
        if (list.get(n).getParameterTypes().length != parameters.length) {
          list.remove(n);
        }
      }
      if (list.isEmpty()) {
        throw new NotBoundException(serviceName + "." + methodName);
      }
      if (list.size() != 1) {
        throw new NotBoundException(serviceName + "." + methodName + "["
          + list.size() + "]");
      }
      Method method = list.get(0);
      try {
        Object result = method.invoke(remote, parameters);
        if (result instanceof Serializable == false) {
          throw new InvocationTargetException(new ClassCastException(
            Serializable.class.getName()));
        }
        return (Serializable) result;
      }
      catch (IllegalAccessException e) {
        throw new NotBoundException(e.getMessage());
      }
      catch (IllegalArgumentException e) {
        throw new NotBoundException(e.getMessage());
      }
      catch (InvocationTargetException e) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        e.getCause().printStackTrace(new PrintWriter(bytes, true));
        throw new RemoteException(new String(bytes.toByteArray(), Charset
          .forName("utf-8")));
      }
    }
    catch (NotBoundException e) {
      throw new NotBoundException(serviceName);
    }
  }

}
