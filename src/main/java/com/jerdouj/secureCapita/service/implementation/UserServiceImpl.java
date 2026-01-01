package com.jerdouj.secureCapita.service.implementation;

import com.jerdouj.secureCapita.domain.User;
import com.jerdouj.secureCapita.dto.UserDTO;
import com.jerdouj.secureCapita.dtomapper.UserDTOMapper;
import com.jerdouj.secureCapita.repository.UserRepository;
import com.jerdouj.secureCapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }
}
