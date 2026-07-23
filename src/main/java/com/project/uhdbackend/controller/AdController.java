package com.project.uhdbackend.controller;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uhdbackend.service.AdService;
import com.project.uhdbackend.service.RSAEncryption;
import com.project.uhdbackend.service.UserLoginLogService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class AdController {

	@Value("${privateKeyStr}")
	String privateKeyStr;

	private AdService adService;
	private RSAEncryption RSAEncryption;
	private UserLoginLogService userLoginLogService;

	public AdController(AdService adService, RSAEncryption RSAEncryption, UserLoginLogService userLoginLogService) {
		this.adService = adService;
		this.RSAEncryption = RSAEncryption;
		this.userLoginLogService = userLoginLogService;
	}

	@PostMapping("/login/ad")
	public String login(@RequestBody String body) throws JSONException, Exception {
		JSONObject requestBody = new JSONObject(body);
		JSONObject rtnObject = new JSONObject();
		if (!requestBody.has("id") || !requestBody.has("code")) {
			return rtnObject.toString();
		}
		String uid = requestBody.get("id").toString();
		String word = requestBody.get("code").toString();
		if (uid.equals("") || word.equals("")) {
			return rtnObject.toString();
		}
		rtnObject = adService.adLogin(uid, word);
		userLoginLogService.record(uid, rtnObject);
		rtnObject.remove("errorMsg");
		return rtnObject.toString();
	}

	public String decrypt(String word) {
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return RSAEncryption.decryptWithPrivateKey(word, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
