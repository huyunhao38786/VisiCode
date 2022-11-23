package VisiCode.Security;

import VisiCode.Domain.User;
import VisiCode.Domain.UserRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureWebTestClient
class UserDetailsServiceImplementationTest {

    @Mock
    UserRepository userRepository;

    User u;

    @InjectMocks
    UserDetailsServiceImplementation userDetailsServiceImplementation;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        MockitoAnnotations.openMocks(this);
        u = new User("test", "", new HashSet<>());
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findByUsername(u.getUsername())).thenReturn(Optional.of(u));
        UserDetails ud = userDetailsServiceImplementation.loadUserByUsername(u.getUsername());
        assert (ud instanceof UserDetailsImplementation);
        UserDetailsImplementation udi = (UserDetailsImplementation) ud;

        assertEquals(udi.getUsername(), u.getUsername());
        assertEquals(udi.getPassword(), u.getPassword());
        assertEquals(udi.getId(), u.getId());

        MatcherAssert.assertThat(udi.getAuthorities(), Matchers.hasSize(1));

        assert (udi.isEnabled());
        assert (udi.isAccountNonExpired());
        assert (udi.isAccountNonLocked());
        assert (udi.isCredentialsNonExpired());

        assert (udi.equals(UserDetailsImplementation.build(u)));
    }

    @Test
    void loadUserByUsernameNonExistent() {
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails ud = userDetailsServiceImplementation.loadUserByUsername(u.getUsername());
        });
    }
}