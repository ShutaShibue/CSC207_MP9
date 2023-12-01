import java.io.*;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
  static class CustomReader {
    private final Reader source;
    private boolean unread = false;
    private int lastChar =  -1;
    public CustomReader(Reader source) {
      this.source = source;
    }
    public int read() throws IOException {
      if (unread) {
        unread = false;
      } else {
        lastChar = source.read();
      }
      return lastChar;
    }

    public void unread() {
      unread = true;
    }
  }
  // +---------------+-----------------------------------------------
  // | Static fields |
  // +---------------+

  /**
   * The current position in the input.
   */
  static int pos;

  // +----------------+----------------------------------------------
  // | Static methods |
  // +----------------+

  /**
   * Parse a string into JSON.
   */
  public static JSONValue parse(String source) throws Exception {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws Exception {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws Exception {
    CustomReader customSource = new CustomReader(source);
    pos = 0;
    JSONValue result = parseKernel(customSource);
    if (-1 != skipWhitespace(customSource)) {
      throw new ParseException("Characters remain at end", pos);
    }
    return result;
  } // parse(Reader)

  // +---------------+-----------------------------------------------
  // | Local helpers |
  // +---------------+

  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  static JSONValue parseKernel(CustomReader source) throws Exception {
    int ch = skipWhitespace(source);
    if (ch == -1) {
      throw new ParseException("Unexpected end of file", pos);
    }
    JSONValue result;
    if (ch == '\"') {
      result = parseString(source);
    } else if (ch == '[') {
      result = parseArray(source);
    } else if (ch == '{') {
      result = parseHash(source);
    } else if (ch == 't' || ch == 'f' || ch == 'n') {
      result = parseConstant(source, ch);
    } else if (Character.isDigit(ch) || ch == '-') {
      result = parseNumber(source, ch);
    } else {
      throw new Exception("invalid json");
    }
    return result;
  }

  static JSONValue parseNumber(CustomReader source, int ch) throws Exception {
    StringBuilder numStr = new StringBuilder();
    boolean decimal = false;
    numStr.append((char) ch);
    ch = source.read();
    while (Character.isDigit(ch) || ch == '.') {
      if (ch == '.' && !decimal) {
        numStr.append((char) ch);
        decimal = true;
      } else if (ch == '.') {
        throw new Exception("invalid json");
      } else {
        numStr.append((char) ch);
      }
      ch = source.read();
    }
    // read non-digit character, need to unread it
    if (ch == ',' || ch == ']' || ch == '}') {
      source.unread();
      pos--;
    }
    if (decimal) {
      return new JSONReal(numStr.toString());
    } else {
      return new JSONInteger(numStr.toString());
    }
  }

  static JSONString parseString(CustomReader source) throws IOException {
    int character = skipWhitespace(source);
    StringBuilder result = new StringBuilder();
    while (character != '\"') {
      result.append((char) character);
      character = source.read();
      ++pos;
    }
    return new JSONString(result.toString());
  }

  static JSONValue parseConstant(CustomReader source, int ch) throws Exception {
    if (ch == 't') {
      for (int i = 1; i < 4; i++) {
        if (source.read() != "true".charAt(i)) {
          throw new Exception("invalid json");
        }
        pos++;
      }
      return JSONConstant.TRUE;
    }
    if (ch == 'f') {
      for (int i = 1; i < 5; i++) {
        if (source.read() != "false".charAt(i)) {
          throw new Exception("invalid json");
        }
        pos++;
      }
      return JSONConstant.FALSE;
    }
    if (ch == 'n') {
      for (int i = 1; i < 4; i++) {
        if (source.read() != "null".charAt(i)) {
          throw new Exception("invalid json");
        }
        pos++;
      }
      return JSONConstant.NULL;
    } else
      throw new Exception("invalid json");
  }

  static JSONValue parseArray(CustomReader source) throws Exception {
    JSONArray result = new JSONArray();
    int ch;
    while (true) {
      JSONValue value = parseKernel(source);
      result.add(value);
      ch = skipWhitespace(source);
      if (ch == ',') {
      } else if (ch == ']') {
        break;
      } else {
        throw new Exception("Expected ',' or ']' after value");
      }
    }
    return result;
  }

  static JSONValue parseHash(CustomReader source) throws Exception {
    JSONHash result = new JSONHash();
    int ch;
    while (true) {
      ch = skipWhitespace(source);
      if (ch != '\"') {
        throw new Exception("Expected '\"' at start of key");
      }
      String key = parseString(source).getValue();
      ch = skipWhitespace(source);
      if (ch != ':') {
        throw new Exception("Expected ':' after key");
      }
      JSONValue value = parseKernel(source);
      result.set(new JSONString(key), value);
      ch = skipWhitespace(source);
      if (ch == ',') {
      } else if (ch == '}') {
        return result;
      } else {
        throw new Exception("Expected ',' or '}' after value");
      }
    }
  }

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(CustomReader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    System.out.println((char) ch);
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
