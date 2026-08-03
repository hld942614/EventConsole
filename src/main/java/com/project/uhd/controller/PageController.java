package com.project.uhd.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.uhd.authentication.CustomUserDetails;

@Controller
public class PageController {

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("errorMessage", "Invalid username or password");
		}
		return "login";
	}

	@GetMapping("/index")
	public String index(@AuthenticationPrincipal CustomUserDetails user, Model model) {
		model.addAttribute("userName", user.getChineseName());
		model.addAttribute("id", user.getId());
		return "index"; // 要導入的html
	}

	@GetMapping("/typeSetting")
	public String typeSetting() {
		return "typeSetting"; // 要導入的html
	}
}
