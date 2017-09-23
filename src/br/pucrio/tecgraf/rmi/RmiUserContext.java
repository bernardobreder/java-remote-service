package br.pucrio.tecgraf.rmi;

import java.util.HashMap;
import java.util.Set;

/**
 * Classe que possui todas as informações do usuário que está requisitando o
 * método.
 * 
 * @author Tecgraf
 */
public class RmiUserContext {

  /** Contextos */
  private static final ThreadLocal<RmiUserContext> contexts =
    new ThreadLocal<RmiUserContext>();
  /** Header */
  private final HashMap<String, Object> header = new HashMap<String, Object>();

  /**
   * @return header
   */
  public HashMap<String, Object> getHeader() {
    return header;
  }

  /**
   * @param key
   * @return valor do header
   */
  public String getString(String key) {
    return (String) this.header.get(key);
  }

  /**
   * @param key
   * @return valor do header
   */
  public Object getObject(String key) {
    return this.header.get(key);
  }

  /**
   * @param key
   * @param value
   */
  public void set(String key, Object value) {
    this.header.put(key, value);
  }

  /**
   * @return chaves
   */
  public Set<String> getKeys() {
    return this.header.keySet();
  }

  /**
   * @return contexto do usuário
   */
  public static RmiUserContext get() {
    return contexts.get();
  }

  /**
   * @param context
   */
  public static void set(RmiUserContext context) {
    contexts.set(context);
  }

  /**
   * Remove o contexto
   */
  public static void remove() {
    contexts.remove();
  }

}
