package com.jerdouj.secureCapita.repository.implementation;

import com.jerdouj.secureCapita.domain.Role;
import com.jerdouj.secureCapita.exception.ApiException;
import com.jerdouj.secureCapita.repository.RoleRepository;
import com.jerdouj.secureCapita.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.jerdouj.secureCapita.query.RoleQuery.*;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;


@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role>{
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("adding role {} to user:{}", roleName, userId);
        try {
            Role role = (Role) jdbc.queryForObject(SELECT_ROLE_BY_Name_QUERY, of("roleName", roleName), new RoleRowMapper());

            if (role == null || role.getId() == null) {
                log.error("Role found but id is null for roleName='{}'", roleName);
                throw new ApiException("No valid role id found for role: " + roleName);
            }

            int updated = jdbc.update(INSERT_ROLE_TO_USER_QUERY, of("user_id", userId, "role_id", requireNonNull(role.getId())));
            log.info("Role assigned successfully: userId={} roleId={} updatedRows={}", userId, role.getId(), updated);

        }catch (EmptyResultDataAccessException exception) {
            log.error("No role found with name: {}", roleName);
            throw new ApiException("No role found by name: " + roleName, exception);
           // throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception exception) {
            log.error("Error while assigning role '{}' to user {}: {}", roleName, userId, exception.getMessage(), exception);
            throw new ApiException("Failed to assign role '" + roleName + "' to user " + userId + ": " + exception.getMessage(), exception);
            //throw new ApiException("An error occurred while creating the user. Please try again later.");

        }

    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("getting role by userId: {}", userId);
        try {
            return (Role) jdbc.queryForObject(SELECT_ROLE_BY_USER_ID_QUERY, of("user_id", userId), new RoleRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            log.error("No role found for userId: {}", userId);
            throw new ApiException("No role found for userId: " + userId, exception);
        } catch (Exception exception) {
            log.error("Error while retrieving role for userId {}: {}", userId, exception.getMessage(), exception);
            throw new ApiException("Failed to retrieve role for userId " + userId + ": " + exception.getMessage(), exception);
        }
    }

    @Override
    public Role getRoleById(Long id) {
        return null;
    }

    @Override
    public Role getRoleByEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
