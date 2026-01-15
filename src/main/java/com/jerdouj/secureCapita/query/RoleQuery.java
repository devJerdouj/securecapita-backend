package com.jerdouj.secureCapita.query;

public class RoleQuery {
    public static final String SELECT_ROLE_BY_Name_QUERY = "SELECT * FROM roles WHERE name = :roleName" ;
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO user_roles (user_id, role_id) VALUES (:user_id, :role_id)";
    public static final String SELECT_ROLE_BY_USER_ID_QUERY = "SELECT r.* FROM roles r " +
            "JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = :user_id";

}
