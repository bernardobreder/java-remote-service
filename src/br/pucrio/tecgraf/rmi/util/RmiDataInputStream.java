package br.pucrio.tecgraf.rmi.util;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;

/**
 * 
 * 
 * @author Tecgraf
 */
public class RmiDataInputStream extends DataInputStream {

  /**
   * @param in
   */
  public RmiDataInputStream(InputStream in) {
    super(in);
  }

  /**
   * @return objeto
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Object readObject() throws IOException, ClassNotFoundException {
    return new ObjectInputStream(this.in).readObject();
  }

  /**
   * @throws IOException
   */
  public void readEof() throws IOException {
    if (this.in.read() != 0xFF) {
      throw new EOFException("expected <eof>");
    }
  }

  /**
   * @return erro
   */
  public RemoteException readRemoteException() {
    try {
      return new RemoteException(this.readUTF());
    }
    catch (IOException e) {
      return new RemoteException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
  }

  /**
   * @throws IOException
   */
  public void skipToEof() throws IOException {
    while (in.read() != 0xFF) {
    }
  }

}
