package com.jerdouj.secureCapita.repository;

import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.Collection;


public interface UserRepository<T extends User> {
     /* Basic CRUD operations */

    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More specific operations */
    User getUserByEmail(String email);

    void sendMfaCode(UserDTO user);
}
