package com.smartplugins.smartplugins.resource;

import com.smartplugins.smartplugins.evente.RecursoCriadoEvent;
import com.smartplugins.smartplugins.model.User;
import com.smartplugins.smartplugins.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/smartplugins/user")
public class UserResource {

    private UserLoginRepository userLoginRepository;
    private ApplicationEventPublisher publisher;

    @Autowired
    public UserResource(UserLoginRepository userLoginRepository, ApplicationEventPublisher publisher) {
        this.userLoginRepository = userLoginRepository;
        this.publisher = publisher;
    }

    @GetMapping()
    public List<User> getUser() {
        return this.userLoginRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, HttpServletResponse response) {
        User userCreate = this.userLoginRepository.save(user);

        this.publisher.publishEvent(new RecursoCriadoEvent(this, response, userCreate.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        User user = this.userLoginRepository.findOne(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        this.userLoginRepository.delete(id);
    }
}
