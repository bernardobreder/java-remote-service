package br.pucrio.tecgraf.rmi.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Utilitário de proxy
 * 
 * @author Tecgraf
 */
public class ProxyUtil {

  /**
   * @param c
   * @param h
   * @return proxy
   */
  @SuppressWarnings("unchecked")
  public static <E> E createProxy(Class<E> c, InvocationHandler h) {
    return (E) Proxy.newProxyInstance(c.getClassLoader(), new Class<?>[] { c },
      h);
  }

}
