package com.project.uhd.service;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.project.uhd.authentication.CustomUserDetails;

@Service
public class UhdUserOidcService extends OidcUserService {

	private static final Logger logger = LoggerFactory.getLogger(UhdUserOidcService.class);
	private static final String checkPermissionErrorMessage = "Check permission for user %s failed: %s.";

	public UhdUserOidcService() {

	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

		final OidcUser oidcUser = super.loadUser(userRequest);
		final String username = oidcUser.getAttribute("sub");
		final boolean isWhiteList = true;

		// 這邊要串......ldap資訊，所以要靠.....idp(oidc)那邊輸出資料
		logger.warn(String.format("Account [%s] is logging in. whitelist=%s.", username, isWhiteList));
		if (isWhiteList) {

			final Set<GrantedAuthority> authorities = buildSetGrantedAuthority(username);

			return new CustomUserDetails(username, "N/A", oidcUser, authorities);
		}

		return null;
	}

	private Set<GrantedAuthority> buildSetGrantedAuthority(final String username) {

		Set<GrantedAuthority> result = new HashSet<>();

		return result;
	}

}
