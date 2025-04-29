package com.drawquest.services.impl;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.exceptions.DuplicateResourceException;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.UserMapper;
import com.drawquest.models.User;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        return UserMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with user name " + username + " not found"));
        return UserMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        User newUser = UserMapper.toUserEntity(userCreateDTO);

        try {
            return UserMapper.toUserResponseDTO(userRepository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("El nombre de usuario o el email ya están en uso.");
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserResponseDTO) // convertir cada entidad a DTO
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));


        existingUser.setLevel(userUpdateDTO.getLevel());
        existingUser.setXp(userUpdateDTO.getXp());
        existingUser.setProgress(userUpdateDTO.getProgress());
        existingUser.setRoles(userUpdateDTO.getRoles());

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(existingUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with name " + username + " not found"));

        return UserDetailsImpl.build(user);
    }
}

