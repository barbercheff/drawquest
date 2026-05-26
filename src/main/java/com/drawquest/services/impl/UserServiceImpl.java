package com.drawquest.services.impl;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.enums.ERole;
import com.drawquest.exceptions.DuplicateResourceException;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.UserMapper;
import com.drawquest.models.Role;
import com.drawquest.models.User;
import com.drawquest.repositories.RoleRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserResponseDTO getUserById(Long id, String username) {
        User user = userRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        return UserMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
        return UserMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        User newUser = UserMapper.toUserEntity(userCreateDTO);
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default user role not found"));
        newUser.getRoles().add(defaultRole);

        try {
            return UserMapper.toUserResponseDTO(userRepository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Username or email is already in use.");
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers(String username) {
        return Collections.singletonList(getUserByUsername(username));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO, String username) {
        User existingUser = userRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));

        existingUser.setLevel(userUpdateDTO.getLevel());
        existingUser.setXp(userUpdateDTO.getXp());

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id, String username) {
        User existingUser = userRepository.findByIdAndUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(existingUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));

        return UserDetailsImpl.build(user);
    }
}
