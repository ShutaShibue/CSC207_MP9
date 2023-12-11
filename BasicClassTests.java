import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicClassTests {
  @Test
  public void StringTest() {
    JSONValue str1 = new JSONString("hello");
    JSONValue str2 = new JSONString("hello");
    JSONValue str3 = new JSONString("aello");

    assertEquals(true, str1.equals(str2));
    assertEquals(false, str1.equals(str3));
    assertEquals("\"hello\"", str1.toString());
  }

  @Test
  public void IntegerTest() {
    JSONValue str1 = new JSONInteger(10);
    JSONValue str2 = new JSONInteger("10");
    JSONValue str3 = new JSONInteger("20");

    assertEquals(true, str1.equals(str2));
    assertEquals(false, str1.equals(str3));
    assertEquals("10", str1.toString());
  }

  @Test
  public void RealTest() {
    JSONValue real1 = new JSONReal(10.0);
    JSONValue real2 = new JSONReal("10.0");
    JSONValue int1 = new JSONInteger("10");

    assertEquals(true, real1.equals(real2));
    assertEquals(false, real1.equals(int1));
    assertEquals("10.0", real1.toString());
  }

  @Test
  public void ArrayTest() {
    JSONArray array = new JSONArray();

    array.add(new JSONString("1"));
    array.add(new JSONInteger(2));
    array.add(new JSONInteger(3));

    JSONArray array2 = new JSONArray();

    array2.add(new JSONString("1"));
    array2.add(new JSONInteger(2));
    array2.add(new JSONInteger(3));
    System.out.println(array.hashCode());
    System.out.println(array2.hashCode());

    assertEquals(true, array.equals(array2));
    assertEquals("[\"1\",2,3]", array.toString());
  }
}
