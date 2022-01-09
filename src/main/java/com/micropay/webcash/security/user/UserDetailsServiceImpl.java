package com.micropay.webcash.security.user;


import com.micropay.webcash.entity.User;
import com.micropay.webcash.repositories.security.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        List<User> employee = employeeRepository.findUserByLoginId(username);

        if (employee.isEmpty()) {
            throw new UsernameNotFoundException("Could not find employee");
        }

        return new UserDetailData(employee.get(0));
    }
}
