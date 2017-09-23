package br.pucrio.tecgraf.rmi.binary.data;

import java.io.Serializable;

import br.pucrio.tecgraf.rmi.IRmiServer;
import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Comando binario
 * 
 * @author Tecgraf
 */
public abstract class AbstractBinaryCommand implements DataSerializable {

  /** Opcode */
  public static final int QUIT_OPCODE = 1;
  /** Opcode */
  public static final int HEADER_OPCODE = 2;
  /** Opcode */
  public static final int LOOKUP_OPCODE = 3;
  /** Opcode */
  public static final int LIST_OPCODE = 4;
  /** Opcode */
  public static final int CALL_OPCODE = 5;

  /**
   * @param server
   * @param context
   * @return retorno
   * @throws Throwable
   */
  public abstract Serializable execute(IRmiServer server, RmiUserContext context)
    throws Throwable;

}
