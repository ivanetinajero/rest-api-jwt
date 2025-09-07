
package dev.itinajero.app.security;

import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 6. Configuración de seguridad y rutas protegidas
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando la seguridad de la aplicación");
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth                
                .requestMatchers("/", "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("Filtro JWT agregado y rutas protegidas configuradas");
        return http.build();
    }

	@Bean
	UserDetailsManager users(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select u.username, u.password, u.estatus from Usuarios u where u.username = ?");
		users.setAuthoritiesByUsernameQuery("select u.username, p.perfil from UsuariosPerfiles up " + 
											"inner join Usuarios u on u.id = up.idUsuario " + 
											"inner join Perfiles p on p.id = up.idPerfil " +
											"where u.username = ?");
		
		return users;
	}

	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Bean PasswordEncoder (BCrypt) creado");
        return new BCryptPasswordEncoder();
    }

}
