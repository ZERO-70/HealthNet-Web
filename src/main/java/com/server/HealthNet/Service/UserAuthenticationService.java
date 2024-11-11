package com.server.HealthNet.Service;

import com.server.HealthNet.Model.UserAuthentication;
import com.server.HealthNet.Repository.UserAuthenticationRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;

    private BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(6);


    public UserAuthenticationService(UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    public int createUser(UserAuthentication userAuthentication) {
        userAuthentication.setPassword(bcrypt.encode(userAuthentication.getPassword()));
        return userAuthenticationRepository.save(userAuthentication);
    }

    public UserAuthentication getUserByUsername(String username) {
        return userAuthenticationRepository.findByUsername(username);
    }

    public List<UserAuthentication> getAllUsers() {
        return userAuthenticationRepository.findAll();
    }

    public int updateUser(UserAuthentication userAuthentication) {
        userAuthentication.setPassword(bcrypt.encode(userAuthentication.getPassword()));
        return userAuthenticationRepository.update(userAuthentication);
    }

    public int deleteUser(String username) {
        return userAuthenticationRepository.deleteByUsername(username);
    }
}
