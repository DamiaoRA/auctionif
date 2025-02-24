package br.edu.ifpb.util;

import static br.edu.ifpb.util.Constants.ADMIN;
import static br.edu.ifpb.util.Constants.MANAGER;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final JwtAuthFilter jwtAuthFilter;
	
	public WebConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	// Permite CORS para qualquer endpoint que comece com "/api/"
    	registry.addMapping("/**")  
        	.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
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
        			.requestMatchers(HttpMethod.POST, "/api/user/register").hasRole(ADMIN)
        			.requestMatchers(HttpMethod.POST, "/api/user/basicregister").permitAll()
        			.requestMatchers(HttpMethod.DELETE, "/api/user/delete/**").hasRole(ADMIN)
        			.requestMatchers("/api/user/list").hasAnyRole(ADMIN, MANAGER)
        			.requestMatchers(HttpMethod.PUT, "/api/user/update/**").hasRole(ADMIN)
        			.requestMatchers(HttpMethod.PUT, "/api/user/updatenorules/**")
        							.hasAnyRole(ADMIN, MANAGER)
        			.anyRequest().authenticated()
//        			.requestMatchers(HttpMethod.PUT, "/api/user/update/**").hasRole("ADMIN")
//        			.anyRequest().permitAll()
        	)
        	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout(
        		(logout) -> logout
        		.clearAuthentication(true)
        		.invalidateHttpSession(true)
        		.logoutUrl("/api/auth/logout")
        		.logoutSuccessHandler(new LogoutSuccessHandler() {
					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
							throws IOException, ServletException {
						//Em caso de sucesso no logout, não faz nada
						response.setStatus(HttpServletResponse.SC_OK);
		                response.getWriter().write("Logout realizado com sucesso!");
					}
				})
        );

        return http.build();
    }
}
