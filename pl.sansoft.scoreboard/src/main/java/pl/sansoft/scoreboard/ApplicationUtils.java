package pl.sansoft.scoreboard;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApplicationUtils {
  
  public static String toHexString(byte[] data){
    if(data==null)
      return "/null/";
    String out = "";
    for (byte b : data) {
      out += String.format("%02x ", b);
    }
    return out;
  }
  
  public static String toHexString(List<Byte> data){
    return data == null ? "/null/" : data.stream().map(x -> String.format("%02x", x)).collect(Collectors.joining(" "));
  }
  
  public static byte[] toByteArray(List<Byte> data) {
    if( data == null)
      return  new byte[0];
    else{
      ByteBuffer buffer = ByteBuffer.allocate(data.size());
      data.forEach(x -> buffer.put(x)); 
      return buffer.array();
    }
  }

  public static void sleep(long ms) {
    try {
      TimeUnit.MILLISECONDS.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }
}
