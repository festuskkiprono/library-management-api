package com.festuskiprono.library.controllers;

import com.festuskiprono.library.dtos.ChangePasswordRequest;
import com.festuskiprono.library.dtos.RegisterUserRequest;
import com.festuskiprono.library.dtos.UpdateUserRequest;
import com.festuskiprono.library.dtos.UserDto;
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


    @GetMapping()
    public Iterable<UserDto> getAllUsers( @RequestParam(required = false,defaultValue ="",name="sort") String sortBy)
    {
        if(!Set.of("name","email").contains(sortBy))
            sortBy = "name";
        return userRepository.findAll()
                .stream()
                .map(user ->userMapper.toDto(user)).toList();
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
        @PostMapping()
    public ResponseEntity <UserDto> addUser(@Valid @RequestBody RegisterUserRequest request,
                                            UriComponentsBuilder builder)
    {
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        var userDto=userMapper.toDto(user);

        var uri = builder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
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
//    @PostMapping
//    public ResponseEntity<?> registerUser(
//            @Valid @RequestBody RegisterUserRequest request,
//            UriComponentsBuilder uriBuilder) {
//
////        if (userRepository.existsByEmail(request.getEmail())) {
////            return ResponseEntity.badRequest().body(
////                    Map.of("email", "Email is already registered.")
////            );
////        }
//
//        var user = userMapper.toEntity(request);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        //user.setRole(Role.USER);
//        userRepository.save(user);
//
//        var userDto = userMapper.toDto(user);
//        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()) .toUri();
//
//        return ResponseEntity.created(uri).body(userDto);
//    }
}
