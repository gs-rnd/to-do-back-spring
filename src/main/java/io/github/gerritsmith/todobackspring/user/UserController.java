package io.github.gerritsmith.todobackspring.user;

import io.github.gerritsmith.todobackspring.security.JwtUtilities;
import io.github.gerritsmith.todobackspring.security.UserDetailsImplementation;
import io.github.gerritsmith.todobackspring.shared.FormatErrorResponse;
import io.github.gerritsmith.todobackspring.user.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilities jwtUtilities;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtilities.generateJwtToken(authentication);
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getUsername(),
                                                 userDetails.getEmail(),
                                                 roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest,
                                          Errors errors) {
        if (userService.usernameExists(registerRequest.getUsername())) {
            errors.rejectValue("username", "Exists", "username already exists");
        }
        if (userService.emailExists(registerRequest.getEmail())) {
            errors.rejectValue("email", "Exists", "email already registered");
        }
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(FormatErrorResponse.springErrorsToApiResponse(errors));
        }
        User user = new User(registerRequest.getUsername(),
                             registerRequest.getEmail(),
                             encoder.encode(registerRequest.getPassword()));
        userService.add(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
