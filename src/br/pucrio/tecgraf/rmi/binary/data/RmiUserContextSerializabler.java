package br.pucrio.tecgraf.rmi.binary.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import br.pucrio.tecgraf.rmi.RmiUserContext;

/**
 * Serializador de {@link RmiUserContext}
 * 
 * @author Tecgraf
 */
public class RmiUserContextSerializabler implements DataSerializable {

  /** Data */
  private final RmiUserContext data;

  /**
   * Construtor
   */
  public RmiUserContextSerializabler() {
    this(new RmiUserContext());
  }

  /**
   * @param data
   */
  public RmiUserContextSerializabler(RmiUserContext data) {
    this.data = data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(DataInputStream input) throws IOException {
    int size = input.readInt();
    for (int n = 0; n < size; n++) {
      String key = input.readUTF();
      String value = input.readUTF();
      this.data.set(key, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(DataOutputStream output) throws IOException {
    Set<String> keys = this.data.getKeys();
    output.writeInt(keys.size());
    for (String key : keys) {
      Object value = this.data.getObject(key);
      if (value != null) {
        output.writeUTF(key);
        output.writeUTF(value.toString());
      }
    }
  }

  /**
   * @return data
   */
  public RmiUserContext getData() {
    return data;
  }

}
