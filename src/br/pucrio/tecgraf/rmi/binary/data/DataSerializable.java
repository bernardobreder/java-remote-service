package br.pucrio.tecgraf.rmi.binary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 
 * 
 * @author Tecgraf
 */
public interface DataSerializable {

  /**
   * @param input
   * @throws IOException
   */
  public void read(DataInputStream input) throws IOException;

  /**
   * @param output
   * @throws IOException
   */
  public void write(DataOutputStream output) throws IOException;

}
