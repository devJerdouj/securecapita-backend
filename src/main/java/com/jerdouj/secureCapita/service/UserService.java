package com.jerdouj.secureCapita.service;

import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.dto.UserDTO;

public interface UserService {
    UserDTO createUser (User user);
}
