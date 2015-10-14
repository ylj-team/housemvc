package ylj.house.mvc.controllers.dosomething;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class RSAUtils {

	/*
	 * static String Modulus_hex =
	 * "00bbea718283a23953a59cf2f14c177a7d1bc82c6109b6e97f7a495c3869b1db1e546971de9a3a13d711b444deee67bb6c9f215d7e89ae3e1366b45ddef8be3c9a02920324e98d0f9dffc8414ce6e6e12b986c62fa60e5b4149ab0f693c2cc68165dc861030adcdede7589d5ba1fa1cedf8bdf4d1565db7ee264ae37633621aeef"
	 * ; static String Public_exponent_hex = "010001"; static String
	 * Private_exponent_hex =
	 * "02c2243c68363f652cef2ad9c3e62c541dce48687c3e051b6bee1bbe703ebe1aa9de8a5f5c20321e5c122b58a2633f6b0ec2ec9e68e2f7e24d05a4c31b1f9fc0148189be60630edbbaa90a1c0cfeb5ff5b7a8fc11fa0e22fb27d3c3a689afac4641bbfee1b2a5d7afcb48449b3a58f493551056cf6d3a63393bee03b6f28db01"
	 * ;
	 */

	//java生成的key
	  static String Modulus_hex ="00" +
	  "bbea718283a23953a59cf2f14c177a7d1bc82c6109b6e97f7a495c3869b1db1e" +
	  "546971de9a3a13d711b444deee67bb6c9f215d7e89ae3e1366b45ddef8be3c9a" +
	  "02920324e98d0f9dffc8414ce6e6e12b986c62fa60e5b4149ab0f693c2cc6816" +
	 "5dc861030adcdede7589d5ba1fa1cedf8bdf4d1565db7ee264ae37633621aeef";
	  
	  static String Public_exponent_hex = "010001";
	  
	  static String Private_exponent_hex ="" +
	  "02c2243c68363f652cef2ad9c3e62c541dce48687c3e051b6bee1bbe703ebe1a" +
	  "a9de8a5f5c20321e5c122b58a2633f6b0ec2ec9e68e2f7e24d05a4c31b1f9fc0" +
	  "148189be60630edbbaa90a1c0cfeb5ff5b7a8fc11fa0e22fb27d3c3a689afac4" +
	  "641bbfee1b2a5d7afcb48449b3a58f493551056cf6d3a63393bee03b6f28db01";
	
	  /*
	  static String Private_exponent_hex ="00" +
			  "02c2243c68363f652cef2ad9c3e62c541dce48687c3e051b6bee1bbe703ebe1a" +
			  "a9de8a5f5c20321e5c122b58a2633f6b0ec2ec9e68e2f7e24d05a4c31b1f9fc0" +
			  "148189be60630edbbaa90a1c0cfeb5ff5b7a8fc11fa0e22fb27d3c3a689afac4" +
			  "641bbfee1b2a5d7afcb48449b3a58f493551056cf6d3a63393bee03b6f28db01";
			  */
			
	  
	  static byte[] Modulus_byte= Hex.decode(Modulus_hex.getBytes());
	  static byte[] Public_exponent_byte= Hex.decode(Public_exponent_hex.getBytes());
	  static byte[] Private_exponent_byte= Hex.decode(Private_exponent_hex.getBytes());
		
	  static RSAPublicKey rsaPublicKey=null;
	  static RSAPrivateKey rsaPrivateKey=null;
	  
	public  static  void init() {
		  
		  try {			  
			rsaPublicKey=generateRSAPublicKey(Modulus_byte,Public_exponent_byte);			 
			rsaPrivateKey=generateRSAPrivateKey(Modulus_byte, Private_exponent_byte);			
		  } catch (Exception e) {			
			  e.printStackTrace();
		  }
		  
	  }
	/*
	// 要加 Modulus_hex Private_exponent_hex 00？
	static String Modulus_hex = "00" + "a5261939975948bb7a58dffe5ff54e65f0498f9175f5a09288810b8975871e99" + "af3b5dd94057b0fc07535f5f97444504fa35169d461d0d30cf0192e307727c06"
			+ "5168c788771c561a9400fb49175e9e6aa4e23fe11af69e9412dd23b0cb6684c4" + "c2429bce139e848ab26d0829073351f4acd36074eafd036a5eb83359d2a698d3";

	static String Public_exponent_hex = "010001";

	static String Private_exponent_hex = "00" + "8e9912f6d3645894e8d38cb58c0db81ff516cf4c7e5a14c7f1eddb1459d2cded"
			+ "4d8d293fc97aee6aefb861859c8b6a3d1dfe710463e1f9ddc72048c09751971c"
			+ "4a580aa51eb523357a3cc48d31cfad1d4a165066ed92d4748fb6571211da5cb1"
			+ "4bc11b6e2df7c1a559e6d5ac1cd5c94703a22891464fba23d0d965086277a161";
	*/
	
	static String KEY_ALGORITHM = "RSA";
	static String Cipher_ALGORITHM="RSA";

	//static String Cipher_ALGORITHM = "RSA/NONE/NoPadding";

	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {

		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}

	}

	/**
	 * 生成私钥
	 * 
	 * @param modulus
	 * @param privateExponent
	 * @return RSAPrivateKey
	 * @throws Exception
	 */

	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}
		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            加密的密钥
	 * @param data
	 *            待加密的明文数据
	 * @return 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(Key key, byte[] data) throws Exception {

		// X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		// 密钥（Key 类型的不透明加密密钥 <------->成密钥规范（底层密钥材料的透明表示）
		// KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		// //通过算法名 返回加密和解密提供密码功能的cipher
		// 对数据加密

		Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);// 用公钥初始化这个cipher

		return cipher.doFinal(data);
	}
	public static byte[] encrypt(byte[] data) throws Exception {

		Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);// 用公钥初始化这个cipher

		return cipher.doFinal(data);
	}
	
	public  static byte[] decrypt(byte[] data) throws Exception {

		Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);// 用公钥初始化这个cipher

		return cipher.doFinal(data);
	}
	
	
	/**
	 * 解密
	 * 
	 * @param key
	 *            解密的密钥
	 * @param raw
	 *            已经加密的数据
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public static byte[] decrypt(Key key, byte[] data) throws Exception {

		// Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		return cipher.doFinal(data);

	}

	public static void main(String[] args) throws Exception {

		Security.addProvider(new BouncyCastleProvider());

		System.out.println("----------------------");

		byte[] modulus = Hex.decode(Modulus_hex.getBytes());
		byte[] publicExponent = Hex.decode(Public_exponent_hex.getBytes());
		;
		byte[] privateExponent = Hex.decode(Private_exponent_hex.getBytes());
		;

		RSAPublicKey pubKey = generateRSAPublicKey(modulus, publicExponent);
		RSAPrivateKey priKey = generateRSAPrivateKey(modulus, privateExponent);

		String text = "test";

		System.out.println("原文=" + text);

		long startTime = System.currentTimeMillis();

		String ciphertext = new String(Hex.encode(encrypt(pubKey, text.getBytes())));

		/*
		String ciphertext = "672eacfce098f3ad8b5bcc79a28f4f7e32fa708c7182fd64f509fb63bea00c38dc9271cfc6b80454e83b6bb9bfb8eafc09ee630e7adecb0f583255ab8fbdb314d3683975303587adab9585ad8241f478466b3f1956c180490c5b1508867b9ffa095ee057c87553c65210644a7a9abc2491678c2d32e7f5231aed7b8eb5f9a23b"
				+ ""
				+ ""
				+ "";
		
		*/
		
		System.out.println("加密后=" + ciphertext);
		long costTime = System.currentTimeMillis() - startTime;

		System.out.println("cost:" + costTime + "'ms");

		startTime = System.currentTimeMillis();
		String decryptText = new String(decrypt(priKey, Hex.decode(ciphertext.getBytes())));

		System.out.println("解密后=" + decryptText);
		costTime = System.currentTimeMillis() - startTime;
		System.out.println("cost:" + costTime + "'ms");

	}
}
