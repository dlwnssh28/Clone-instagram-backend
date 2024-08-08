package com.instagram.clone.auth;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SignupController {
	
	private final SignupService signupService;
	
	public SignupController(SignupService joinService) {
         this.signupService = joinService;
    }
	
	@PostMapping("/signup")
	public String signup(@RequestBody SignupDTO signupDTO) {
		signupService.signup(signupDTO);
		return "ok";
	}
	
	@GetMapping("/")
    public String mainP() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller : "+ username + ", Role : "+role;
    }
	
	@GetMapping("/admin")
    public String adminP() {

        return "admin";
    }
}
