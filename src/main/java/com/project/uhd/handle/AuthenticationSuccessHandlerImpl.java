package com.project.uhd.handle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
//		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//		securityContext.setAuthentication(authentication);
//		SecurityContextHolder.setContext(securityContext);
//
//		HttpSession session = request.getSession();
//		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
//
//		System.out.println(authentication);
//		ObjectNode result = objectMapper.createObjectNode();
//		result.put("message", "登入成功");
//		result.put("token", true);
//		System.out.println(result);
//		response.setContentType("application/json");
//		response.setCharacterEncoding("UTF-8");
//		response.getWriter().write(result.toString());
//		response.getWriter().flush();
		response.sendRedirect("/index");
	}
}
