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

  static JSONValue parseString(Reader source) throws IOException {
    char character;
    StringBuilder result = new StringBuilder();
    while ((character = (char) skipWhitespace(source)) != '\"') {
      result.append(character);
    }
    return new JSONString(result.toString());
  }

  static JSONValue parseNumber(Reader source) throws IOException {
    int ch;
    boolean isInt = true;
    StringBuilder array = new StringBuilder();
    while ((ch = (char) skipWhitespace(source)) != -1 && isNumber(ch)) {
      array.append(ch);
      if (ch == '.')
        isInt = false;
    }
    if (isInt)
      return new JSONInteger(array.toString());
    else
      return new JSONReal(array.toString());
  }

  static JSONValue parseConstant(Reader source) throws Exception {
    StringBuilder str = new StringBuilder();
    int ch;
    while ((ch = skipWhitespace(source)) != ']') {
      str.append(ch);
    }
    if (str.toString() == "TRUE")
      return JSONConstant.TRUE;
    if (str.toString() == "FALSE")
      return JSONConstant.FALSE;
    if (str.toString() == "NULL")
      return JSONConstant.NULL;
    else
      throw new Exception();
  }

  static JSONValue parseArray(Reader source) throws ParseException, IOException {
    StringBuilder str = new StringBuilder();
    JSONArray array = new JSONArray();
    int ch;
    while ((ch = skipWhitespace(source)) != ']') {
      str.append(ch);
    }
    String[] elements = str.toString().split(",");
    for (String string : elements) {
      array.add(parse(string));
    }
    return array;
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
