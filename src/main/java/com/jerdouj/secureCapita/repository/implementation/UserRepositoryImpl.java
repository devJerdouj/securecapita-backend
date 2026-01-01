package com.jerdouj.secureCapita.repository.implementation;

import com.jerdouj.secureCapita.domain.Role;
import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.exception.ApiException;
import com.jerdouj.secureCapita.repository.RoleRepository;
import com.jerdouj.secureCapita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.jerdouj.secureCapita.enumeration.RoleType.ROLE_USER;
import static com.jerdouj.secureCapita.enumeration.VerificationType.ACCOUNT;
import static com.jerdouj.secureCapita.query.UserQuery.*;

import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<User> {

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    @Transactional
    public User create(User user) {
        // check if user with email already exists
        // if exists, throw exception
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use, please use a different email and try again.");
        // else, save new user to database
        try {
            user.setCreatedAt(Instant.now());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            SqlParameterSource paramSource = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, paramSource, keyHolder);
            user.setId(requireNonNull(keyHolder.getKey()).longValue());
            // add roles to user (user_roles table )
            try {
                roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            } catch (Exception e) {
                log.error("Failed to add role '{}' to user id {}: {}", ROLE_USER.name(), user.getId(), e.getMessage(), e);
                throw new ApiException("Failed to assign role to user. See logs for details.", e);
            }
            // send verification email
            // save URL to verification table with expiration date
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSER_ACCOUNT_VERIFICATION_URL_QUERY, of("userId", user.getId(),
                    "url", verificationUrl));
            log.info("Insert user verification url: {}", verificationUrl);
            // send verication url to user's email
            //emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNonLocked(true);
            // return saved user
            return user;
            // if any errors, throw exception with proper message
        } catch (Exception exception) {
                log.error("An error occurred while creating user: {}", exception.getMessage(), exception);
                throw new ApiException("An error occurred while creating the user. Please try again later.", exception);

        }




    }



    @Override
    public Collection<User> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private Integer getEmailCount(String email) {
           return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail().trim().toLowerCase())
                .addValue("password", encoder.encode(user.getPassword()));

    }


    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();



    }


}
