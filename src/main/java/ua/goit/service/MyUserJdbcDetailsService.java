package ua.goit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.goit.model.dto.UserDTO;

import java.util.Collection;
import java.util.stream.Collectors;
@Service(value = "userServiceDetails")
@RequiredArgsConstructor
public class MyUserJdbcDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            UserDTO userDTO = userService.findByUsername(username);
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return userDTO.getRoles().stream()
                            .map(roleDAO ->
                                    new SimpleGrantedAuthority(roleDAO.getName()))
                            .collect(Collectors.toList());
                }

                @Override
                public String getPassword() {
                    return userDTO.getPassword();
                }

                @Override
                public String getUsername() {
                    return userDTO.getEmail();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        } catch (ResponseStatusException e) {
            throw new UsernameNotFoundException(username);
        }

    }
}
