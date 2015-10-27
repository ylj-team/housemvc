package ylj.house.mvc.controllers.dosomething;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

public class TestRSAKey {

	public static void main(String[] args) throws Exception{
		
		System.out.println("BouncyCastle init ...");	
		Security.addProvider(new BouncyCastleProvider());

		RSAUtils.init();
		
		String text = "test";

		System.out.println("原文=" + text);

		long startTime = System.currentTimeMillis();

		String ciphertext = new String(Hex.encode(RSAUtils.encrypt(text.getBytes())));
		
		 ciphertext="97691814603c3dc8c2c793df5c55145dd46f0a88565a7830609b9711efbfc9c00429ea0a248ac8ee957fc5c170f05ce3620f101f90578e5fe70148f0bbd148f933df6717c5c1edfd272af832855806b939152a6fcb0df42114c42c845813efd39fe24c976ef345b17f715d79e323fe0702353a778bb12f0f73e3b4e7e57cc1d2";
		
		String ptext = new String(RSAUtils.decrypt(Hex.decode(ciphertext.getBytes())));
	
		System.out.println("text:"+text);
		System.out.println("ciphertext:"+ciphertext);
		System.out.println("ptext:"+ptext);
	}
}
