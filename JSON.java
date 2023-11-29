import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import org.junit.platform.commons.util.StringUtils;

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
    if (-1 == ch) {
      throw new ParseException("Unexpected end of file", pos);
    }

    int depth = 0; // number of { - }. when 0, end of Json stream.
    do {
      if(ch == '{'){
        depth ++;
      }
      if(ch == '['){
        ArrayList<String> chars = new ArrayList<String>();
        while (true) {
          int i = source.read();
          if(i == ']') break;
          chars.add(Character.toString(i));
        }

        // elements within array
        String[] elements = String.join("", chars).split(",");
        JSONValue arr = new JSONArray();
        for (String elem : elements) {
          
        }
      }
      
    } while (depth != 0);
    throw new ParseException("Unimplemented", pos);
  } // parseKernel

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
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
