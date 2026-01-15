
/*
  --- General rules for SQL schema files ---
    1. Use underscore_names instead of camelCase.
    2. Table names should be plural.
    3. Spell out id fields (item_id instead of id).
    4. Don't use ambiguous column names.
    5. Name foreign key columns the same as the columns they refer to .
    6. Use caps for all SQL queries.
 */

CREATE SCHEMA IF NOT EXISTS securecapita;
SET NAMES 'utf8mb4';
SET TIME_ZONE = '+00:00';
USE securecapita;


DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS user_events;
DROP TABLE IF EXISTS account_verifications;
DROP TABLE IF EXISTS reset_password_verifications;
DROP TABLE IF EXISTS two_factor_verifications;

CREATE TABLE users (
                       id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name  VARCHAR(50) NOT NULL,
                       email      VARCHAR(100) NOT NULL,
                       password   VARCHAR(255) DEFAULT NULL,
                       address    VARCHAR(255) DEFAULT NULL,
                       phone      VARCHAR(30) DEFAULT NULL,
                       title      VARCHAR(50) DEFAULT NULL,
                       bio        VARCHAR(255) DEFAULT NULL,
                       enabled    TINYINT(1) DEFAULT 0,
                       non_locked TINYINT(1) DEFAULT 1,
                       using_mfa  TINYINT(1) DEFAULT 0,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       image_url  VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
                       CONSTRAINT Uq_Users_Email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE roles (
                       id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       name        VARCHAR(50) NOT NULL,
                       permissions VARCHAR(255) NOT NULL,
                       CONSTRAINT Uq_Roles_Name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE user_roles (
                            id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT UNSIGNED NOT NULL,
                            role_id BIGINT UNSIGNED NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
                            CONSTRAINT Uq_User_roles_User_id UNIQUE (user_id)
) ENGINE=InnoDB;


CREATE TABLE events (
                        id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        type        VARCHAR(50) NOT NULL ,
                        description VARCHAR(255) NOT NULL,
                        CONSTRAINT Uq_Events_Type UNIQUE (type)
) ENGINE=InnoDB;

CREATE TABLE user_events (
                             id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             user_id    BIGINT UNSIGNED NOT NULL,
                             event_id   BIGINT UNSIGNED NOT NULL,
                             device     VARCHAR(100) DEFAULT NULL,
                             ip_address VARCHAR(100) DEFAULT NULL,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
                             FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE account_verifications (
                                       id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT UNSIGNED NOT NULL,
                                       url     VARCHAR(255) NOT NULL,
                                       FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
                                       CONSTRAINT Uq_Account_verifications_User_Id UNIQUE (user_id),
                                       CONSTRAINT uq_Account_verifications_Url UNIQUE (url)
) ENGINE=InnoDB;

CREATE TABLE reset_password_verifications (
                                              id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                              user_id         BIGINT UNSIGNED NOT NULL,
                                              url             VARCHAR(255) NOT NULL,
                                              expiration_date TIMESTAMP NOT NULL,
                                              FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
                                              CONSTRAINT uq_Reset_password_verifications_User_id UNIQUE (user_id),
                                              CONSTRAINT uq_Reset_password_verifications_Url UNIQUE (url)
) ENGINE=InnoDB;

CREATE TABLE two_factor_verifications (
                                          id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                          user_id         BIGINT UNSIGNED NOT NULL,
                                          code            VARCHAR(10) NOT NULL,
                                          expiration_date TIMESTAMP NOT NULL,
                                          FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
                                          CONSTRAINT uq_Two_factor_verifications_User_id UNIQUE (user_id),
                                          CONSTRAINT uq_Two_factor_verifications_Code UNIQUE (code)
) ENGINE=InnoDB;