package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.UserAuthentication;
import com.server.HealthNet.Service.UserAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user_authentications")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserAuthentication userAuthentication) {
        userAuthenticationService.createUser(userAuthentication);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserAuthentication> getUserByUsername(@PathVariable String username) {
        UserAuthentication userAuthentication = userAuthenticationService.getUserByUsername(username);
        return userAuthentication != null ? ResponseEntity.ok(userAuthentication) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<UserAuthentication> getAllUsers() {
        return userAuthenticationService.getAllUsers();
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UserAuthentication userAuthentication) {
        userAuthentication.setUsername(username); // Ensure the username is set to match the URL
        userAuthenticationService.updateUser(userAuthentication);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userAuthenticationService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
