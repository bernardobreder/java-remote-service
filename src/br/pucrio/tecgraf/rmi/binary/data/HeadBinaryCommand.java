package br.pucrio.tecgraf.rmi.binary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Comando look
 * 
 * @author Tecgraf
 */
public class HeadBinaryCommand extends AbstractBinaryCommand {

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(DataInputStream input) throws IOException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutputStream output) throws IOException {
    output.writeInt(AbstractBinaryCommand.HEADER_OPCODE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Serializable execute(IRmiServer server, RmiUserContext context)
    throws Throwable {
    return context.getHeader();
  }

}
