package com.github.kgrech.statcollectior.server.config;

import com.github.kgrech.statcollectior.server.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provider of users credentials for security config
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Component
public class ClientsDetailsService implements UserDetailsService {

    private Set<String> clientIds;

    /**
     * Sets all configured clients
     * @param clients clients list
     */
    @Autowired
    public void setClients(List<Client> clients) {
        this.clientIds = clients.stream().map(Client::getKey).collect(Collectors.toSet());
    }

    /**
     * Check if clients exists anf grants user permissions to him
     * @param userName client key
     * @return user access details
     * @throws UsernameNotFoundException in case of wrong client key provided
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (clientIds.contains(userName)) {
            Collection<GrantedAuthority> authorities = new ArrayList<>(1);
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(userName, userName, true, true, true, true, authorities);
        }
        throw new UsernameNotFoundException("Client " + userName + " was not found!");
    }
}
