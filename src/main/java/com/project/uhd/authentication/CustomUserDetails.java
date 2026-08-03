package com.project.uhd.authentication;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class CustomUserDetails implements UserDetails, OidcUser {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String chineseName;
	private String id;
	private Collection<? extends GrantedAuthority> authorities;
	private OidcUser oidcUser;

	public CustomUserDetails(String username, String password, String chineseName, String id,
			Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.password = password;
		this.chineseName = chineseName;
		this.id = id;
		this.authorities = authorities;
	}

	public CustomUserDetails(String username, String password, OidcUser oidcUser,
			Collection<? extends GrantedAuthority> authorities) {
		this.oidcUser = oidcUser;

		this.username = username;
		this.password = password;
		this.chineseName = oidcUser.getAttribute("name");
		this.id = oidcUser.getSubject();
		this.authorities = authorities;
	}

	public String getChineseName() {
		return chineseName;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getClaims() {
		// TODO Auto-generated method stub
		return oidcUser.getClaims();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return oidcUser.getUserInfo();
	}

	@Override
	public OidcIdToken getIdToken() {
		// TODO Auto-generated method stub
		return oidcUser.getIdToken();
	}
}
