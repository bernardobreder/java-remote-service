package br.pucrio.tecgraf.rmi.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 
 * 
 * @author Tecgraf
 */
public class RmiDataOutputStream extends DataOutputStream {

  /**
   * @param out
   */
  public RmiDataOutputStream(OutputStream out) {
    super(out);
  }

  /**
   * @param obj
   * @throws IOException
   */
  public void writeObject(Object obj) throws IOException {
    new ObjectOutputStream(this.out).writeObject(obj);
  }

  /**
   * @throws IOException
   */
  public void writeEof() throws IOException {
    this.out.write(0xFF);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }

}
