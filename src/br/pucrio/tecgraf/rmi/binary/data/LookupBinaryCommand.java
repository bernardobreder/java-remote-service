package br.pucrio.tecgraf.rmi.binary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Comando look
 * 
 * @author Tecgraf
 */
public class LookupBinaryCommand extends AbstractBinaryCommand {

  /** Nome */
  private String name;

  /**
   * Construtor
   */
  public LookupBinaryCommand() {
    super();
  }

  /**
   * @param name
   */
  public LookupBinaryCommand(String name) {
    super();
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(DataInputStream input) throws IOException {
    boolean hasService = input.readBoolean();
    if (hasService) {
      this.name = input.readUTF();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutputStream output) throws IOException {
    output.writeInt(AbstractBinaryCommand.LOOKUP_OPCODE);
    output.writeBoolean(this.name != null);
    if (this.name != null) {
      output.writeUTF(this.name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable execute(IRmiServer server, RmiUserContext context)
    throws Throwable {
    String[] list = server.list();
    for (String name : list) {
      if (name.equals(this.name)) {
        return name;
      }
    }
    throw new NotBoundException("not found " + this.name);
  }

}
