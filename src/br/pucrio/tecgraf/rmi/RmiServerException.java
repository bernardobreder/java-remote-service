package br.pucrio.tecgraf.rmi;

/**
 * 
 * 
 * @author Tecgraf
 */
public class RmiServerException extends Exception {

  /**
   * @param cause
   */
  public RmiServerException(Throwable cause) {
    super(toString(cause, 0));
  }

  /**
   * @param cause
   * @param level
   * @return text
   */
  private static String toString(Throwable cause, int level) {
    StringBuilder sb = new StringBuilder();
    for (int l = 0; l < level; l++) {
      sb.append('\t');
    }
    sb.append(cause.getClass().getName());
    sb.append(": ");
    sb.append(cause.getMessage());
    Throwable[] throwables = cause.getSuppressed();
    for (int n = 0; n < throwables.length; n++) {
      for (int l = 0; l < level; l++) {
        sb.append('\t');
      }
      sb.append(throwables[n]);
      sb.append('\n');
    }
    if (cause.getCause() != null) {
      sb.append(toString(cause.getCause(), level + 1));
    }
    return sb.toString();
  }

}
