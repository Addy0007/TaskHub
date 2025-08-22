package com.aithinkers.TaskHub.Service;


import com.aithinkers.TaskHub.Enum.Role;
import com.aithinkers.TaskHub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    // ⚠️ must be *exactly* this name/signature (lowercase 'l')
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var u = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + email));

        String authority = (u.getRole() == Role.ADMIN) ? "ROLE_ADMIN" : "ROLE_USER";

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())     // username = email
                .password(u.getPassword())      // BCrypt from DB
                .authorities(authority)         // or .roles("ADMIN"/"USER")
                .build();
    }

}


