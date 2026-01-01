package com.jerdouj.secureCapita.repository;

import com.jerdouj.secureCapita.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.Collection;


public interface RoleRepository<T extends Role> {
    /* Basic CRUD operations */

    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More specific operations */
    void addRoleToUser(Long userId, String roleName);
    Role getRoleById(Long id);
    Role getRoleByEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
