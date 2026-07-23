package com.project.uhdbackend.service;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdService {
	@Value("${ad.login.url}")
	private String LOGIN_URL;
	private String BASEDN;
	private String PRINCIPAL;
	private String SEARCHFIELD;
	private final String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private final Control[] connCtls = null;
	private final List<String> whiteList = Arrays.asList("I38234", "I37157", "I37160", "069757", "438520", "438532",
			"425668", "578764", "088632", "438621", "169003", "165038", "451028", "157127", "081916", "408170",
			"649515");

	public JSONObject adLogin(String uid, String pwd) {
		JSONObject return_json = new JSONObject();
		LdapContext ctx = null;
		Hashtable<String, String> env = setEnv(uid, pwd);
		try {
			ctx = new InitialLdapContext(env, connCtls);

			if (ctx != null) {
				System.out.println(uid + " 登入");
				SearchControls searchCtls = new SearchControls();
				searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
				String searchFilter = "(&(objectCategory=Person)(" + SEARCHFIELD + "=" + uid + "))";
				String returnedAtts[] = { "sAMAccountName", "givenName", "cn", "mail", "userPrincipalName",
						"userCertificate", "userPassword", "objectSid", "sn", "description", "company",
						"physicalDeliveryOfficeName", "department" };

				searchCtls.setReturningAttributes(returnedAtts);

				NamingEnumeration<SearchResult> empInfo = ctx.search(BASEDN, searchFilter, searchCtls);

				if (empInfo.hasMore()) {
					Attributes attrs = empInfo.next().getAttributes();
//						System.out.println("attrs = " + attrs);
					String dept = attrs.get("physicalDeliveryOfficeName").get().toString();
					// 國內人事代號，海外沒有
					String id = attrs.get("sAMAccountName").get().toString();
					if (dept.contains("COM") || dept.contains("SY5") || whiteList.contains(id)) {
						// 國內name
						String cName = attrs.get("description").get().toString();
						// 海外name
//						String eName = attrs.get("sn").get().toString();
						// 公司
						String company = attrs.get("company").get().toString();
						// email
						String email = "";
						if (attrs.get("mail") != null)
							email = attrs.get("mail").get().toString();
						else
							email = attrs.get("userPrincipalName").get().toString();
						return_json = returnJsonBuilder(id, cName, company, email);
					} else {
						System.out.println("user has no authority.");
						return_json.put("action", "N");
						return_json.put("data", "");
						return_json.put("rtnMsg", "user has no authority");
					}
				} else {
					System.out.println("user not found.");
					return_json.put("action", "N");
					return_json.put("data", "");
					return_json.put("rtnMsg", "user not found");
				}
				ctx.close();
			}
//			} catch (AuthenticationException ex) {
//				ex.printStackTrace();
//                /**
//                 * error Code 說明 : 
//                 * 525 : 用戶沒有找到 
//                 * 52e : 證號不正確 
//                 * 530 : 此時間不允許登入(not permitted to logon at this time) 
//                 * 532 : 密碼期滿 
//                 * 533 : 帳號不可用 
//                 * 701 : 帳戶期滿 
//                 * 773 : 用戶必須重設密碼
//                 */
//			} catch (NamingException e) {
//				e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return_json.put("action", "N");
			return_json.put("data", "");
			return_json.put("rtnMsg", "ad fail");
			return_json.put("errorMsg", e.getMessage());
			return return_json;
		}
		return return_json;
	}

	private Hashtable<String, String> setEnv(String uid, String pwd) {
		this.BASEDN = "dc=emctaiwan,dc=com";
		this.PRINCIPAL = "@emctaiwan.com";
		this.SEARCHFIELD = "sAMAccountName";
		Hashtable<String, String> env = new Hashtable<String, String>();
		// 使用的Driver名稱
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		// 連線的位置
		env.put(Context.PROVIDER_URL, LOGIN_URL);
		// 認證的方式使用 simple
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		// 認證的戶名
		env.put(Context.SECURITY_PRINCIPAL, uid + PRINCIPAL);
		// 認證的密碼
		env.put(Context.SECURITY_CREDENTIALS, pwd);

		return env;
	}

	private JSONObject returnJsonBuilder(String id, String name, String company, String email) {
		JSONObject data = new JSONObject();
		data.put("id", id);
		data.put("name", name);
		data.put("company", company);
		data.put("email", email);
		JSONObject return_json = new JSONObject();
		return_json.put("action", "Y");
		return_json.put("data", data);
		return_json.put("rtnMsg", "OK");
		return return_json;
	}

}
