package com.project.uhd.authentication;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.project.uhd.service.AdService;
import com.project.uhd.service.UserLoginLogService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LogManager.getLogger(CustomAuthenticationProvider.class);
	
	private AdService adService;
	private UserLoginLogService userLoginLogService;

	public CustomAuthenticationProvider(AdService adService, UserLoginLogService userLoginLogService) {
		super();
		this.adService = adService;
		this.userLoginLogService = userLoginLogService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String secret = authentication.getCredentials().toString();
		JSONObject response = adService.adLogin(userName, secret);
		userLoginLogService.record(userName, response);
		response.remove("errorMsg");
		String action = response.optString("action", "N");
		if (!action.equals("Y")) {
			logger.error("AD login failed, response : " + response);
			throw new BadCredentialsException("Invalid username or password");
		}
		String chineseName = response.getJSONObject("data").getString("name");
		String id = response.getJSONObject("data").getString("id");
		CustomUserDetails userDetails = new CustomUserDetails(userName, secret, chineseName, id,
				List.of(new SimpleGrantedAuthority("USER")));
		UsernamePasswordAuthenticationToken UPAtoken = new UsernamePasswordAuthenticationToken(userDetails, secret,
				userDetails.getAuthorities());
		return UPAtoken;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

//	private JSONObject adLogin(JSONObject userInfo) {
//		JSONObject response = new JSONObject();
//		HttpURLConnection connection = null;
//		BufferedReader br = null;
//		try {
//			URL url = new URL(apiServer + "/api/v1/login/ad");
//			connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Content-Type", "application/json");
//			connection.setRequestProperty("Accept", "application/json");
//			connection.setUseCaches(false);
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//
//			OutputStream os = connection.getOutputStream();
//			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//			writer.write(userInfo.toString());
//			writer.flush();
//			writer.close();
//			os.close();
//
//			connection.connect();
//
//			int responseCode = connection.getResponseCode();
//			if (responseCode == 200) {
//				br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//				StringBuffer data = new StringBuffer();
//				String inputLine;
//				while ((inputLine = br.readLine()) != null) {
//					data.append(inputLine);
//				}
//				response = new JSONObject(data.toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
}
