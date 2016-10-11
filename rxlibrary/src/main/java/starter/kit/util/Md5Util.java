package starter.kit.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

  public static String md5(String string) {
    byte[] hash1;
    try {
      hash1 = string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException var3) {
      throw new RuntimeException("Huh,UTF-8 should be supported?", var3);
    }

    return computeMD5(hash1);
  }

  public static String computeMD5(byte[] input) {
    try {
      if (null == input) {
        return null;
      }
      MessageDigest instance = MessageDigest.getInstance("MD5");
      instance.update(input, 0, input.length);
      byte[] md5bytes = instance.digest();
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < md5bytes.length; ++i) {
        String hex = Integer.toHexString(255 & md5bytes[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();
    } catch (NoSuchAlgorithmException exception) {
      throw new RuntimeException(exception);
    }
  }
}
