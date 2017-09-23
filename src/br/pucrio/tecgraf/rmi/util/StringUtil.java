package br.pucrio.tecgraf.rmi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe utilitária
 * 
 * @author Tecgraf
 */
public class StringUtil {

  /**
   * @param output
   * @param line
   * @throws IOException
   */
  public static void writeLine(OutputStream output, String line)
    throws IOException {
    int size = line.length();
    for (int n = 0; n < size; n++) {
      char c = line.charAt(n);
      if (c <= 0x7F) {
        output.write(c);
      }
      else if (c <= 0x7FF) {
        output.write(((c >> 6) & 0x1F) + 0xC0);
        output.write((c & 0x3F) + 0x80);
      }
      else {
        output.write(((c >> 12) & 0xF) + 0xE0);
        output.write(((c >> 6) & 0x3F) + 0x80);
        output.write((c & 0x3F) + 0x80);
      }
    }
    output.write('\n');
  }

  /**
   * @param input
   * @return line
   * @throws IOException
   */
  public static String readLine(InputStream input) throws IOException {
    StringBuilder sb = new StringBuilder();
    for (;;) {
      int c = input.read();
      if (c <= 0x7F) {
      }
      else if ((c >> 5) == 0x6) {
        int i2 = input.read();
        c = ((c & 0x1F) << 6) + (i2 & 0x3F);
      }
      else {
        int i2 = input.read();
        int i3 = input.read();
        c = ((c & 0xF) << 12) + ((i2 & 0x3F) << 6) + (i3 & 0x3F);
      }
      if (c == -1 || c == '\n') {
        break;
      }
      sb.append((char) c);
    }
    return sb.toString();
  }

  /**
   * @param value
   * @return id
   */
  public static String createId(Object value) {
    String hash = "" + value.hashCode();
    try {
      byte[] digest = MessageDigest.getInstance("md5").digest(hash.getBytes());
      StringBuilder sb = new StringBuilder(digest.length);
      for (int n = 0; n < digest.length; n++) {
        String hex = Integer.toHexString(digest[n] + 128);
        if (hex.length() == 1) {
          hex = "0" + hex;
        }
        sb.append(hex);
      }
      return sb.toString().toUpperCase();
    }
    catch (NoSuchAlgorithmException e) {
      return hash;
    }
  }

}
