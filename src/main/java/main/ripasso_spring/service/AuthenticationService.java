package main.ripasso_spring.service;

import main.ripasso_spring.model.dto.AuthenticationRequest;
import main.ripasso_spring.model.dto.AuthenticationResponse;
import main.ripasso_spring.model.dto.RefreshTokenRequest;
import main.ripasso_spring.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
        private final JwtService jwtService;
        private final UserDetailsService userDetailsService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationService(
                        JwtService jwtService,
                        UserDetailsService userDetailsService,
                        AuthenticationManager authenticationManager) {
                this.jwtService = jwtService;
                this.userDetailsService = userDetailsService;
                this.authenticationManager = authenticationManager;
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                // search for user in database
                UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
                // generate tokens
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
        }

        public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
                String refreshToken = request.getRefreshToken();
                String userEmail = jwtService.extractUsername(refreshToken);

                if (userEmail != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                        if (jwtService.isTokenValid(refreshToken, userDetails)) {
                                String accessToken = jwtService.generateAccessToken(userDetails);
                                return AuthenticationResponse.builder()
                                                .accessToken(accessToken)
                                                .refreshToken(refreshToken) // Reuse the same refresh token
                                                .build();
                        }
                }

                throw new IllegalStateException("Invalid refresh token");
        }
}
