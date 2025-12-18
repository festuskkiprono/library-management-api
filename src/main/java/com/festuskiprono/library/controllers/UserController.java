package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.ChangePasswordRequest;
import com.festuskiprono.library.dtos.RegisterUserRequest;
import com.festuskiprono.library.dtos.UpdateUserRequest;
import com.festuskiprono.library.dtos.UserDto;
import com.festuskiprono.library.entities.Role;
import com.festuskiprono.library.entities.User;
import com.festuskiprono.library.mappers.UserMapper;
import com.festuskiprono.library.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    /*@GetMapping()
    public Iterable<UserDto> getAllUsers( @RequestParam(required = false,defaultValue ="",name="sort") String sortBy)
    {
        if(!Set.of("name","email").contains(sortBy))
            sortBy = "name";
        return userRepository.findAll()
                .stream()
                .map(user ->userMapper.toDto(user)).toList();
    }*/
    @GetMapping()
    public ResponseEntity<?> getAllUsers( @RequestParam(required = false,defaultValue ="",name="sort") String sortBy)
    {
        if(!Set.of("name","email").contains(sortBy))
            sortBy = "name";
        try {
            var users =  userRepository.findAll()
                    .stream()
                    .map(user ->userMapper.toDto(user)).toList();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id)
    {
        var user = userRepository.findById(id).orElse(null);
        if(user == null)
            return ResponseEntity.notFound().build();
        var userDto = new UserDto(user.getId(),user.getName(),user.getEmail());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name="id") long id,@RequestBody UpdateUserRequest request)
    {
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        System.out.println("This is the user "+ user);
        userMapper.update(request,user);
        System.out.println("This is the request "+ request);

        System.out.println(" AFTER MAPPER UPDATE");
        System.out.println("This is the user " + user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id)
    {
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/{id}/change-Password")
    public ResponseEntity<Void> changePassword(
            @PathVariable long id,
            @RequestBody ChangePasswordRequest request)
    {
        var user=userRepository.findById(id).orElse(null);
        if(user==null)
        {
            return ResponseEntity.notFound().build();
        }

        if(!user.getPassword().equals(request.getOldPassword()))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();

    }
    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {

        // Prevent duplicate registrations
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(null);
        }

        // Map request to entity
        var user = userMapper.toEntity(request);

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role
        user.setRole(Role.USER);

        // Persist user
        userRepository.save(user);

        // Map to DTO to avoid exposing sensitive data
        var userDto = userMapper.toDto(user);

        // Build Location header: /users/{id}
        var uri = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(userDto.getId())
                .toUri();

        // Return 201 Created with user data
        return ResponseEntity.created(uri).body(userDto);
    }


}
