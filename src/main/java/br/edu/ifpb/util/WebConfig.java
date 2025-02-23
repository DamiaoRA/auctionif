package br.edu.ifpb.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
//	@Autowired
	private final JwtAuthFilter jwtAuthFilter;//TODO

//	@Autowired
    private final UserService userService;
	
	public WebConfig(JwtAuthFilter jwtAuthFilter, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")  // Permite CORS para qualquer endpoint que comece com "/api/"
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
//        registry.addMapping("/api/**")  // Permite CORS para qualquer endpoint que comece com "/api/"
        //.allowedOrigins("http://localhost:5173");  // Permite requisições apenas deste domínio
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth
        			.requestMatchers("/api/auth/**").permitAll()
        			.requestMatchers("/api/user/save").permitAll()
        			.requestMatchers(HttpMethod.DELETE, "/api/user/delete/**")
        				.hasRole("ADMIN")
        			.anyRequest().authenticated()
//        			.anyRequest().permitAll()
        	)
        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout(
        		(logout) -> logout
        		.clearAuthentication(true)
        		.invalidateHttpSession(true)
        		.logoutUrl("api/logout") //TODO
        		.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						//Em caso de sucesso no logout, não faz nada
					}
				})
        );

        return http.build();
    }
}
