package ylj.house.mvc.controllers.dosomething;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Base64;

public class MessageDigestUtil {
	
	public  static String Algorithm_MD5="MD5";
	public  static String Algorithm_SHA_1="SHA-1";
	
	
	public static String byteArrayToHex(byte[] byteArray) {
		// 首先初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
		char[] resultCharArray = new char[byteArray.length * 2];
		// 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		// 字符数组组合成字符串返回
		return new String(resultCharArray);
	}

	public static byte[] digest(byte[] content, String algorithm) throws NoSuchAlgorithmException {

		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

		messageDigest.update(content);

		return messageDigest.digest();
	}

	public static String digest(String text, String algorithm) throws NoSuchAlgorithmException {

		byte[] content = null;
		try {
			content = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArrayToHex(digest(content, algorithm));
	}

	public static String md5digest(String text) throws NoSuchAlgorithmException {
		return digest( text, "MD5") ;
	}
	
	public static void main(String[] args) throws Exception {

		String text = "abcdefg";
		String slat="1234567";
		text=text+slat;
		byte[] plainText = text.getBytes("UTF8");
		// 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
		// MessageDigest messageDigest=MessageDigest.getInstance("SHA-1");
		// MD5是一种消息摘要算法(Message Digest Algorithm)。另外还有一种常用的消息摘要算法SHA1
		MessageDigest messageDigest1 = MessageDigest.getInstance("MD5");
		System.out.println("" + messageDigest1.getProvider());

		MessageDigest messageDigest2 = MessageDigest.getInstance("MD5");
		System.out.println("" + messageDigest2.getProvider());
		// 开始使用算法

		System.out.println("" + (messageDigest1 == messageDigest2));

		System.out.println(digest(text, Algorithm_MD5));
		System.out.println(digest(text, Algorithm_SHA_1));
	//	System.out.println(digest(text, "MD5"));
	
	}
}
