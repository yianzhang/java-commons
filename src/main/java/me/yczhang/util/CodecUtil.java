package me.yczhang.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by YCZhang on 9/9/15.
 */
public class CodecUtil {

	private final static String[] DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

	/**
	 * MD5编码
	 * @param src 原byte数组
	 * @return 编码byte数组
	 */
	public static byte[] md5Encode(byte[] src) {
		if (src == null)
			return null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(src);
			return bytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * byte数组转换成16进制字符串
	 * @param bytes byte数组
	 * @return 16进制字符串
	 */
	public static String bytes2HexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(byte2HexString(b));
		}
		return sb.toString();
	}

	private static String byte2HexString(byte b) {
		int i = b;
		i = i < 0 ? i + 256 : i;
		return DIGITS[i / 16] + DIGITS[i % 16];
	}

	/**
	 * 解析unicode编码
	 * @param unicodeString unicode字符串
	 * @return 解析结果
	 */
	public static String unicode2String(String unicodeString) {
		char aChar;
		int len = unicodeString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len; ) {
			aChar = unicodeString.charAt(x++);
			if (aChar == '\\') {
				aChar = unicodeString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = unicodeString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);

		}
		return outBuffer.toString();
	}

	/**
	 * 将字符串解析成unicode编码
	 * @param str 原字符串
	 * @return 解析结果
	 */
	public static String string2Unicode(String str) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			int strInt = str.charAt(i);
			if(strInt > 127) {
				result.append("\\u").append(Integer.toHexString(strInt));
			} else {
				result.append(str.charAt(i));
			}
		}
		return result.toString();
	}
}
