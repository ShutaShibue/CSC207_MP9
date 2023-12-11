import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseMethodTests {
  @Test
  public void Str() throws Exception {
    String str = "\"hello\"";
    JSONValue val = JSON.parse(str);
    assertEquals("\"hello\"", val.toString());
  }

  @Test
  public void Int() throws Exception {
    String str = "100";
    JSONValue val = JSON.parse(str);
    assertEquals("100", val.toString());
  }

  @Test
  public void Real() throws Exception {
    String str = "100.0";
    JSONValue val = JSON.parse(str);
    assertEquals("100.0", val.toString());
  }

  @Test
  public void Arr() throws Exception {
    String str = "[\"hello\", \"hi\"]";
    JSONValue val = JSON.parse(str);
    assertEquals("[\"hello\",\"hi\"]", val.toString());
  }

  @Test
  public void Const() throws Exception {
    String str = "true";
    JSONValue val = JSON.parse(str);
    assertEquals("true", val.toString());
  }

}
