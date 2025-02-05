import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

/**
 * JSON hashes/objects.
 */
public class JSONHash implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /* the number of values currently in the HashTable */
  int size = 0;

  /* An array of ArrayLists that will contain the Key/Value pairs */
  Object[] buckets;

  /* A random number generator to expand the Array */
  Random rand;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /* Making a new JSONHash with the clear method */
  public JSONHash() {
    this.clear();
  }

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  String getTabs(int n) {
    String ret = "";
    for (int i = 0; i < n; i++) {
      ret += "  ";
    }
    return ret;
  }

  /**
   * Convert to a string (e.g., for printing).
   */
  @SuppressWarnings("unchecked")
  public String toString() {
    String ret = "{";

    Iterator<KVPair<JSONString, JSONValue>> iterate = getValue();
    while (iterate.hasNext()) {
      KVPair<JSONString, JSONValue> newPair = (KVPair<JSONString, JSONValue>) iterate.next();
      ret += newPair.key().toString() + ": " + newPair.value().toString();
      if (iterate.hasNext())
        ret += ", ";
    }
    /* add a closing bracket */
    ret += " }";
    return ret;
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    return this.equals(other);
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return this.hashCode();
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print(this.toString());
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString, JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   */
  @SuppressWarnings("unchecked")
  public JSONValue get(JSONString key) {
    // find the key's index
    int keyIndex = find(key);
    if (this.buckets[keyIndex] != null) {
      ArrayList<KVPair<JSONString, JSONValue>> curList =
          (ArrayList<KVPair<JSONString, JSONValue>>) this.buckets[keyIndex];
      Iterator<KVPair<JSONString, JSONValue>> iterate = curList.listIterator();
      while (iterate.hasNext()) {
        // iterating through the ArrayList to make sure the key doesn't already exist
        KVPair<JSONString, JSONValue> newPair = (KVPair<JSONString, JSONValue>) iterate.next();
        if (newPair.key().equals(key)) {
          return newPair.value();
        } // if
      } // while
    }
    // throw an error if the bucket is null or the key couldn't be found
    throw new Error("The key given does not correspond with any keys in the array\n");
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  @SuppressWarnings("unchecked")
  public Iterator<KVPair<JSONString, JSONValue>> iterator() {
    return new Iterator<KVPair<JSONString, JSONValue>>() {
      int bIndex = 0;
      int aIndex = 0;
      int aIndexCp = aIndex;
      int bIndexCp = bIndex;

      public boolean hasNext() {
        aIndexCp = aIndex;
        bIndexCp = bIndex;
        return hasNextHelper();
      }

      boolean hasNextHelper() {
        if (bIndexCp >= buckets.length) {
          return false;
        } else {
          if (buckets[bIndexCp] != null) {
            ArrayList<KVPair<JSONString, JSONValue>> curList =
                (ArrayList<KVPair<JSONString, JSONValue>>) buckets[bIndexCp];
            if (aIndexCp < curList.size()) {
              return true;
            } else {
              aIndexCp = 0;
              bIndexCp++;
              return hasNextHelper();
            }
          } else {
            aIndexCp = 0;
            bIndexCp++;
            return hasNextHelper();
          }
        }
      }

      public KVPair<JSONString, JSONValue> next() {
        if (bIndex >= buckets.length) {
          return null;
        } else {
          if (buckets[bIndex] != null) {
            ArrayList<KVPair<JSONString, JSONValue>> curList =
                (ArrayList<KVPair<JSONString, JSONValue>>) buckets[bIndex];
            if (aIndex < curList.size()) {
              aIndex++;
              return curList.get(aIndex - 1);
            } else {
              aIndex = 0;
              bIndex++;
              return next();
            }
          } else {
            aIndex = 0;
            bIndex++;
            return next();
          }
        }
      }

    };
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  @SuppressWarnings("unchecked")
  public void set(JSONString key, JSONValue value) {
    // find Key index in the Chained hash table
    int KeyInd = find(key);
    ArrayList<KVPair<JSONString, JSONValue>> curList =
        (ArrayList<KVPair<JSONString, JSONValue>>) this.buckets[KeyInd];
    if (curList == null) {
      // make a new ArrayList and add to it
      ArrayList<KVPair<JSONString, JSONValue>> newList = new ArrayList<>();
      newList.add(new KVPair<JSONString, JSONValue>(key, value));
      this.buckets[KeyInd] = newList;
      this.size++;
    } else {
      Iterator<KVPair<JSONString, JSONValue>> iterate = curList.listIterator();
      while (iterate.hasNext()) {
        // iterating through the ArrayList to make sure the key doesn't already exist
        KVPair<JSONString, JSONValue> newPair = (KVPair<JSONString, JSONValue>) iterate.next();
        if (newPair.key().toString().equals(key.toString())) {
          throw new Error("the key matches with an existing key, indicating erroneous JSON\n");
        } // if
      } // while
      // adding the pair if the key doesn't already exist
      curList.add(new KVPair<JSONString, JSONValue>(key, value));
    } // else
  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return this.size; // STUB
  } // size()

  /* Clearing and initializing the JSONHash */
  public void clear() {
    /* making a new random number generator */
    this.rand = new Random();
    /* setting the bucket array to be 41 initially */
    this.buckets = new Object[41];
    /* setting size to be zero */
    this.size = 0;
  }

  /* find will find the index of the key in the HashTable */

  public int find(JSONString key) {
    return Math.abs(key.hashCode()) % this.buckets.length;
  }
} // class JSONHash
