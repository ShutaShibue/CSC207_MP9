import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.text.ParseException;

public class ParseMethodTests {
  @Test
  public void Str() throws IOException{
    String str = "\"hello\"";
    JSONValue val = JSON.parseString(str);
    assertEquals("\"hello\"", val.toString());
  }

  @Test
  public void Int() throws IOException{
    String str = "100";
    JSONValue val = JSON.parseNumber(str);
    assertEquals("100", val.toString());
  }

  @Test
  public void Real() throws IOException{
    String str = "100.0";
    JSONValue val = JSON.parseNumber(str);
    assertEquals("100.0", val.toString());
  }

  @Test
  public void Arr() throws IOException, ParseException{
    String str = "[1,2,3,4]";
    JSONValue val = JSON.parseArray(str);
    assertEquals("[1,2,3,4]", val.toString());
  }

  @Test
  public void Const() throws Exception {
    String str = "TRUE";
    JSONValue val = JSON.parseConstant(str);
    assertEquals("true", val.toString());
  }

  
}
