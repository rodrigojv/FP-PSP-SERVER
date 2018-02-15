package py.org.fundacionparaguaya.pspserver.security.dtos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;

import java.util.Collection;
import java.util.List;

/**
 * Created by rodrigovillalba on 11/16/17.
 */
public class UserDetailsDTO implements UserDetails {

    private final String password;
    private final  String username;
    private final boolean enabled;
    private final List<GrantedAuthority> grantedAuthorities;
    private final OrganizationDTO organization;
    private final ApplicationDTO application;


    UserDetailsDTO(String username, String password, boolean enabled, List<GrantedAuthority> grantedAuthorities, OrganizationDTO organization, ApplicationDTO application) {
        this.password = password;
        this.username = username;
        this.enabled = enabled;
        this.grantedAuthorities = grantedAuthorities;
        this.organization = organization;
        this.application = application;
    }

    public static UserDetailsDTOBuilder builder() {
        return new UserDetailsDTOBuilder();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // we never lock accounts
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // credentials never expire
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public OrganizationDTO getOrganization() {
        return organization;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public boolean hasRole(Role role) {
        return this.getAuthorities()
                .stream()
                .filter(auth -> Role.valueOf(auth.getAuthority()) == role)
                .count() > 0;
    }
}
