package com.festuskiprono.library.repositories;

import com.festuskiprono.library.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
