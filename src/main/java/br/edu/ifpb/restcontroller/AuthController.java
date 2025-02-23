package br.edu.ifpb.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpb.entity.Login;
import br.edu.ifpb.entity.dto.TokenDTO;
import br.edu.ifpb.entity.dto.UserDTO;
import br.edu.ifpb.service.JwtService;
import br.edu.ifpb.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

//	@Autowired
	protected AuthenticationManager authenticationManager;

//	@Autowired
	protected UserService userService;

//	@Autowired
	private JwtService jwtService;
	
	public AuthController(
			AuthenticationManager authenticationManager, 
			JwtService jwtService,
			UserService userService) {

        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
		try {
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
	
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token = jwtService.generateToken(authentication);
	
	        UserDTO userdto = userService.findDtoByEmail(login.getEmail());
	        TokenDTO tokendto = new TokenDTO(token, userdto);
	
	        return ResponseEntity.ok(tokendto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }

}
