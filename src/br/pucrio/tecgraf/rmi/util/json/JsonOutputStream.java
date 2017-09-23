package br.pucrio.tecgraf.rmi.util.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Escreve objeto no formato json
 * 
 * 
 * @author Tecgraf
 */
public class JsonOutputStream {

  /** Delega a sa�da */
  private final OutputStream output;

  /**
   * Construtor
   * 
   * @param output
   */
  public JsonOutputStream(OutputStream output) {
    this.output = output;
  }

  /**
   * Escreve um objeto
   * 
   * @param object
   * @throws IOException
   */
  public void writeObject(Object object) throws IOException {
    if (object == null) {
      this.writeNull();
    }
    else if (object instanceof String) {
      this.writeString((String) object);
    }
    else if (object instanceof Boolean) {
      this.writeBoolean((Boolean) object);
    }
    else if (object instanceof Integer) {
      this.writeInteger((Integer) object);
    }
    else if (object instanceof Long) {
      this.writeLong((Long) object);
    }
    else if (object instanceof Float) {
      this.writeFloat((Float) object);
    }
    else if (object instanceof Double) {
      this.writeDouble((Double) object);
    }
    else if (object instanceof Number) {
      this.writeNumber((Number) object);
    }
    else if (object instanceof Date) {
      this.writeDate((Date) object);
    }
    else if (object instanceof Throwable) {
      this.writeThrowable((Throwable) object);
    }
    else if (object instanceof ArrayList<?>) {
      this.writeArrayList((ArrayList<?>) object);
    }
    else if (object instanceof Collection<?>) {
      this.writeCollection((Collection<?>) object);
    }
    else if (object instanceof Enum<?>) {
      this.writeEnum((Enum<?>) object);
    }
    else if (object instanceof Map<?, ?>) {
      this.writeMap((Map<?, ?>) object);
    }
    else if (object.getClass().isArray()) {
      this.writeArray((Object[]) object);
    }
    else {
      this.writeStruct(object);
    }
  }

  /**
   * Escreve uma estrutura de dados qualquer
   * 
   * @param object
   * @throws IOException
   * @throws IllegalArgumentException
   */
  public void writeStruct(Object object) throws IOException {
    output.write('{');
    {
      output.write('\"');
      output.write('c');
      output.write('l');
      output.write('a');
      output.write('s');
      output.write('s');
      output.write('\"');
      output.write(':');
      output.write('\"');
      String name = object.getClass().getSimpleName();
      int length = name.length();
      for (int m = 0; m < length; m++) {
        output.write(name.charAt(m));
      }
      output.write('\"');
    }
    Class<?> c = object.getClass();
    while (c != Object.class) {
      Field[] fields = c.getDeclaredFields();
      for (int n = 0; n < fields.length; n++) {
        try {
          Field field = fields[n];
          int modifiers = field.getModifiers();
          if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
            field.setAccessible(true);
            Object value = field.get(object);
            String name = field.getName();
            output.write(',');
            output.write('\"');
            int length = name.length();
            for (int m = 0; m < length; m++) {
              output.write(name.charAt(m));
            }
            output.write('\"');
            output.write(':');
            this.writeObject(value);
          }
        }
        catch (Exception e) {
        }
      }
      c = c.getSuperclass();
    }
    output.write('}');
  }

  /**
   * Imprime o null
   * 
   * @throws IOException
   */
  public void writeNull() throws IOException {
    output.write('n');
    output.write('u');
    output.write('l');
    output.write('l');
  }

  /**
   * Escreve uma String
   * 
   * @param text
   * @throws IOException
   */
  public void writeString(String text) throws IOException {
    output.write('\"');
    int length = text.length();
    for (int n = 0; n < length; n++) {
      char c = text.charAt(n);
      if (c == '\\') {
        output.write('\\');
        output.write('\\');
      }
      else if (c == '/') {
        output.write('\\');
        output.write('/');
      }
      else if (c == '\"') {
        output.write('\\');
        output.write('\"');
      }
      else if (c == '\r') {
        output.write('\\');
        output.write('r');
      }
      else if (c == '\n') {
        output.write('\\');
        output.write('n');
      }
      else if (c == '\t') {
        output.write('\\');
        output.write('t');
      }
      else if (c == '\b') {
        output.write('\\');
        output.write('b');
      }
      else if (c == '\f') {
        output.write('\\');
        output.write('f');
      }
      else if (c <= 0x7F) {
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
    output.write('\"');
  }

  /**
   * Escreve True
   * 
   * @throws IOException
   */
  public void writeTrue() throws IOException {
    output.write('t');
    output.write('r');
    output.write('u');
    output.write('e');
  }

  /**
   * Escreve false
   * 
   * @throws IOException
   */
  public void writeFalse() throws IOException {
    output.write('f');
    output.write('a');
    output.write('l');
    output.write('s');
    output.write('e');
  }

  /**
   * Escreve um Boolean
   * 
   * @param flag
   * @throws IOException
   */
  public void writeBoolean(Boolean flag) throws IOException {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
  }

  /**
   * Escreve um Boolean
   * 
   * @param flag
   * @throws IOException
   */
  public void writeBoolean(boolean flag) throws IOException {
    if (flag) {
      this.writeTrue();
    }
    else {
      this.writeFalse();
    }
  }

  /**
   * Escreve um Integer
   * 
   * @param number
   * @throws IOException
   */
  public void writeInteger(Integer number) throws IOException {
    output.write(number.toString().getBytes());
  }

  /**
   * Escreve um int
   * 
   * @param number
   * @throws IOException
   */
  public void writeInteger(int number) throws IOException {
    output.write(Integer.toString(number).getBytes());
  }

  /**
   * Escreve um Long
   * 
   * @param number
   * @throws IOException
   */
  public void writeLong(Long number) throws IOException {
    output.write(number.toString().getBytes());
  }

  /**
   * Escreve um Long
   * 
   * @param number
   * @throws IOException
   */
  public void writeLong(long number) throws IOException {
    output.write(Long.toString(number).getBytes());
  }

  /**
   * Escreve um Float
   * 
   * @param number
   * @throws IOException
   */
  public void writeFloat(Float number) throws IOException {
    output.write(number.toString().getBytes());
  }

  /**
   * Escreve um float
   * 
   * @param number
   * @throws IOException
   */
  public void writeFloat(float number) throws IOException {
    output.write(Float.toString(number).getBytes());
  }

  /**
   * Escreve um Double
   * 
   * @param number
   * @throws IOException
   */
  public void writeDouble(Double number) throws IOException {
    output.write(number.toString().getBytes());
  }

  /**
   * Escreve um Double
   * 
   * @param number
   * @throws IOException
   */
  public void writeDouble(double number) throws IOException {
    output.write(Double.toString(number).getBytes());
  }

  /**
   * Escreve um número
   * 
   * @param number
   * @throws IOException
   */
  public void writeNumber(Number number) throws IOException {
    output.write(number.toString().getBytes());
  }

  /**
   * Escreve uma data
   * 
   * @param object
   * @throws IOException
   */
  public void writeDate(Date object) throws IOException {
    Calendar c = Calendar.getInstance();
    c.setTime(object);
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH) + 1;
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    int second = c.get(Calendar.SECOND);
    int milisecond = c.get(Calendar.MILLISECOND);
    StringBuilder sb = new StringBuilder(24);
    if (year < 1000) {
      sb.append('0');
    }
    if (year < 100) {
      sb.append('0');
    }
    if (year < 10) {
      sb.append('0');
    }
    sb.append(year);
    sb.append('-');
    if (month < 10) {
      sb.append('0');
    }
    sb.append(month);
    sb.append('-');
    if (day < 10) {
      sb.append('0');
    }
    sb.append(day);
    sb.append('T');
    if (hour < 10) {
      sb.append('0');
    }
    sb.append(hour);
    sb.append(':');
    if (minute < 10) {
      sb.append('0');
    }
    sb.append(minute);
    sb.append(':');
    if (second < 10) {
      sb.append('0');
    }
    sb.append(second);
    sb.append('.');
    if (milisecond < 100) {
      sb.append('0');
    }
    if (milisecond < 10) {
      sb.append('0');
    }
    sb.append(milisecond);
    sb.append('Z');
    this.writeString(sb.toString());
  }

  /**
   * Escreve um erro
   * 
   * @param object
   * @throws IOException
   */
  public void writeThrowable(Throwable object) throws IOException {
    output.write(("{\"class\":\"" + object.getClass().getName()
      + "\",\"detailMessage\":\"" + object.getMessage() + "\"}").getBytes());
  }

  /**
   * Escreve uma lista
   * 
   * @param list
   * @throws IOException
   */
  public void writeArrayList(ArrayList<?> list) throws IOException {
    output.write('[');
    int size = list.size();
    for (int n = 0; n < size; n++) {
      this.writeObject(list.get(n));
      if (n != size - 1) {
        output.write(',');
      }
    }
    output.write(']');
  }

  /**
   * Escreve uma coleção
   * 
   * @param list
   * @throws IOException
   */
  public void writeCollection(Collection<?> list) throws IOException {
    output.write('[');
    Iterator<?> iterator = list.iterator();
    while (iterator.hasNext()) {
      this.writeObject(iterator.next());
      if (iterator.hasNext()) {
        output.write(',');
      }
    }
    output.write(']');
  }

  /**
   * Escreve um enum
   * 
   * @param object
   * @throws IOException
   */
  public void writeEnum(Enum<?> object) throws IOException {
    output.write(("{\"class\":\"" + object.getClass().getSimpleName()
      + "\",\"ordinal\":\"" + Integer.toString(object.ordinal()) + "\"}")
      .getBytes());
  }

  /**
   * Escreve um enum
   * 
   * @param array
   * @throws IOException
   */
  public void writeArray(Object[] array) throws IOException {
    output.write('[');
    int size = array.length;
    for (int n = 0; n < size; n++) {
      this.writeObject(array[n]);
      if (n != size - 1) {
        output.write(',');
      }
    }
    output.write(']');
  }

  /**
   * Escreve um enum
   * 
   * @param map
   * @throws IOException
   */
  public void writeMap(Map<?, ?> map) throws IOException {
    output.write('{');
    Entry<?, ?>[] entrySet = map.entrySet().toArray(new Entry[map.size()]);
    for (int n = 0; n < entrySet.length; n++) {
      Entry<?, ?> entry = entrySet[n];
      this.writeObject(entry.getKey());
      output.write(':');
      this.writeObject(entry.getValue());
      if (n != entrySet.length - 1) {
        output.write(',');
      }
    }
    output.write('}');
  }

}
