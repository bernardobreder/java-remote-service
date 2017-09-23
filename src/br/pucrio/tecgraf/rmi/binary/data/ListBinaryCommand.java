package br.pucrio.tecgraf.rmi.binary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.Remote;
import java.util.ArrayList;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Comando look
 * 
 * @author Tecgraf
 */
public class ListBinaryCommand extends AbstractBinaryCommand {

  /** Service */
  private String service;

  /**
   * Construtor
   */
  public ListBinaryCommand() {
    this(null);
  }

  /**
   * @param service
   */
  public ListBinaryCommand(String service) {
    this.service = service;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(DataInputStream input) throws IOException {
    boolean hasService = input.readBoolean();
    if (hasService) {
      this.service = input.readUTF();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutputStream output) throws IOException {
    output.writeInt(AbstractBinaryCommand.LIST_OPCODE);
    output.writeBoolean(this.service != null);
    if (this.service != null) {
      output.writeUTF(this.service);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable execute(IRmiServer server, RmiUserContext context)
    throws Throwable {
    ArrayList<String> list = new ArrayList<String>();
    if (this.service == null) {
      String[] services = server.list();
      for (String service : services) {
        list.add(service);
      }
    }
    else {
      Remote remote = server.lookup(this.service);
      if (remote == null) {
        return "service not found";
      }
      Class<?> c = remote.getClass();
      while (c != Object.class) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
          if (Modifier.isPublic(method.getModifiers())) {
            if (!Modifier.isStatic(method.getModifiers())) {
              StringBuilder sb = new StringBuilder();
              sb.append(method.getName());
              sb.append('(');
              Class<?>[] parameterTypes = method.getParameterTypes();
              for (int n = 0; n < parameterTypes.length; n++) {
                Class<?> parameterType = parameterTypes[n];
                sb.append(parameterType.getSimpleName());
                if (n != parameterTypes.length - 1) {
                  sb.append(", ");
                }
              }
              sb.append(')');
              list.add(sb.toString());
            }
          }
        }
        c = c.getSuperclass();
      }
    }
    return list.toArray(new String[list.size()]);
  }

}
