package com.project.uhdbackend.service;

import java.security.PrivateKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class RSAEncryption {

//    public KeyPair generateKeyPair() throws Exception {
//        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
//        generator.initialize(2048); // 使用2048位的密钥长度
//        return generator.generateKeyPair();
//    }

//    public String encryptWithPublicKey(String plaintext, PublicKey publicKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }

    public String decryptWithPrivateKey(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

//	public static void main(String[] args) throws Exception {
//		// 生成密钥对
//		KeyPair keyPair = generateKeyPair();
//		PublicKey publicKey = keyPair.getPublic();
//		PrivateKey privateKey = keyPair.getPrivate();
//		System.out.println("publicKey：" + publicKey);
//		System.out.println("privateKey：" + privateKey);
//
//		// 将公钥和私钥转换为Base64编码字符串
//		String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//		String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
//
//		System.out.println("公钥：" + publicKeyStr);
//		System.out.println("私钥：" + privateKeyStr);
//		String s = "Hello";
//		System.out.println(decryptWithPrivateKey(encryptWithPublicKey(s, publicKey), privateKey));
//	}
}
