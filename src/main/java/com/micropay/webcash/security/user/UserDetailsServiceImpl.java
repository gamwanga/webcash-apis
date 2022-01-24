package com.micropay.webcash.security.user;


import com.micropay.webcash.entity.SysUser;
import com.micropay.webcash.repositories.security.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        SysUser employee = employeeRepository.findUserByLoginId(username);

        if (employee == null) {
            throw new UsernameNotFoundException("Could not find employee");
        }

        return new UserDetailData(employee);
    }
}
