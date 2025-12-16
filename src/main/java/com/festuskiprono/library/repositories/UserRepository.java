package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
