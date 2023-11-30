import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Utilities for our simple implementation of JSON.
 */
public class JSON {
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
  public static JSONValue parse(String source) throws ParseException, IOException {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws ParseException, IOException {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws ParseException, IOException {
    pos = 0;
    JSONValue result = parseKernel(source);
    if (-1 != skipWhitespace(source)) {
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
  static JSONValue parseKernel(Reader source) throws ParseException, IOException {
    int ch;
    ch = skipWhitespace(source);
    JSONValue result = new JSONHash();

    // need fixing
    while (ch != -1) {
      char character = (char) ch;

      if (character == '\"') {
        StringBuilder str = new StringBuilder();
        while ((ch = skipWhitespace(source)) != -1) {
          character = (char) ch;
          if (character == '\"') {
            break;
          }
          str.append(character);
        }
        JSONString jsonString = new JSONString(str.toString());
      } else if (character == '[') {
        JSONArray jsonArray = new JSONArray();
        JSONValue jsonValue;
        while ((ch = skipWhitespace(source)) != -1) {
          // do recursively
        }
        return jsonArray;
      }
      ch = skipWhitespace(source);
    }
    return result;
  }

  /**
   * parse String into JSONValue given string.
   */
  static JSONValue parseString(String source) throws IOException {
    return new JSONString(source);
  } // parseString(String)

  /**
   * parse Number (Real, Int) into JSONValue given string.
   */
  static JSONValue parseNumber(String source) throws IOException {
    if (source.contains("."))
      return new JSONReal(source);
    else
      return new JSONInteger(source);
  } // parseNumber



  /**
   * parse Constant into JSONValue given string.
   */
  static JSONValue parseConstant(String source) throws Exception {
    if (source == "TRUE" || source == "true")
      return JSONConstant.TRUE;
    if (source == "FALSE" || source == "false")
      return JSONConstant.FALSE;
    if (source == "NULL" || source == "null")
      return JSONConstant.NULL;
    else
      throw new Exception();
  } // parseConstant(String)

  /**
   * parse Array into JSONValue given string.
   */
  static JSONValue parseArray(String source) throws ParseException, IOException {
    JSONArray array = new JSONArray();
    String[] elements = source.substring(1, source.length() - 1).split(",");
    for (String string : elements) {
      array.add(parse(string));
    }
    return array;
  } // parseArray(String)

  static JSONValue parseHash(Reader source) throws ParseException, IOException {
    StringBuilder str = new StringBuilder();
    JSONHash hash = new JSONHash();
    char ch;
    while ((ch = (char) skipWhitespace(source)) != '{') {
      str.append(ch);
    }
    String[] keyValuePairs = str.toString().split(",");
    for (String pair : keyValuePairs) {
      String[] keyValue = pair.split(": ");
      hash.set(new JSONString(keyValue[0]), parse(keyValue[1]));
    }
    return hash;
  }

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(Reader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return, space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

  static boolean isNumber(int ch) {
    return (ch - '0' >= 0 && ch - '0' <= 9) || (ch == '.');
  }

} // class JSON
