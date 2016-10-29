package com.tys.ms.service;

import com.tys.ms.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    private UserProfileServiceImpl userProfileServiceImpl;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String jobId) throws UsernameNotFoundException {
        User user = userService.findByJobId(jobId);
        logger.info("User : {}", user);
        if (user==null){
            logger.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        if (user.isHasLocked()) {
            logger.info("User HasLocked");
            throw new UsernameNotFoundException("Username not found");
        }
        if (!user.isHasPassed()) {
            logger.info("User HasLocked");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getJobId(), user.getPassword(),
                true, true, true, true, getGrantedAuthorities(user));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        logger.info("UserProfile : {}", user.getUserProfile());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getUserProfile().getType() ) );
        logger.info("authorities : {}", authorities);
        return authorities;
    }
}
