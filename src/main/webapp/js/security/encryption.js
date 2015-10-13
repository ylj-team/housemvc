
	// public key ,hex
 
	var  RSA_e="10001";  
	 
	var  RSA_n= "bbea718283a23953a59cf2f14c177a7d1bc82c6109b6e97f7a495c3869b1db1e"+""
		+ "546971de9a3a13d711b444deee67bb6c9f215d7e89ae3e1366b45ddef8be3c9a"+""
		+ "02920324e98d0f9dffc8414ce6e6e12b986c62fa60e5b4149ab0f693c2cc6816"+""
		+ "5dc861030adcdede7589d5ba1fa1cedf8bdf4d1565db7ee264ae37633621aeef";

	function encryptByPublicKey(plainText) {
		
		
		var rsa = new RSAKey();
		rsa.setPublic(RSA_n, RSA_e);
	
		var res = rsa.encrypt(plainText);	
		return res;
	}
	
	function encrypt(plainText) {
	
		return encryptByPublicKey(plainText);
	}
	