package com.example.inventory.configuration;

import com.example.inventory.User.Role;
import com.example.inventory.User.RoleRepository;
import com.example.inventory.User.User;
import com.example.inventory.User.UserRepository;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    RoleRepository rolerepo;

    @Autowired
    UserRepository userrepo;

private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication;
        String email=token.getPrincipal().getAttributes().get("email").toString();
        if(userrepo.findUserByEmail(email).isPresent()){

        }else{
            User user=new User();
            user.setFirstName(token.getPrincipal().getAttributes().get("first_name").toString());
            user.setLastName(token.getPrincipal().getAttributes().get("last_name").toString());
            user.setEmail(email);
            List<Role> roles=new ArrayList<>();
            roles.add(rolerepo.findById(2).get());
            user.setRoles(roles);
            userrepo.save(user);

        }
        redirectStrategy.sendRedirect(request,response, "/home");
    }
}
