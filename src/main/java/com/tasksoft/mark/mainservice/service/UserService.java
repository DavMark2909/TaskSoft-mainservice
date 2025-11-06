package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.exception.UserNotFoundException;
import com.tasksoft.mark.mainservice.dto.UserCreateDto;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserCreateDto createUserDto) {
        User user = new User();
        user.setFirstName(createUserDto.firstName());
        user.setLastName(createUserDto.lastName());
        user.setUsername(createUserDto.username());
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getUsersById(List<Long> users) {
        return userRepository.findAllById(users);
    }
}
