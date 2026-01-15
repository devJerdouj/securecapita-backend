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
    @Override
    public UserDTO getUserByEmail(String email) {
        // 1. Récupération de l'utilisateur (entité User) depuis le repository
        // 2. Transformation immédiate en UserDTO via le mapper
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendMfaCode(UserDTO user) {
        userRepository.sendMfaCode(user);

    }
}
