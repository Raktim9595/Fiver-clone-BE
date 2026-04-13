package com.raktim.fiverclone.user.service;

import com.raktim.fiverclone.common.DTO.PaginatedResponseDto;
import com.raktim.fiverclone.common.exceptions.BusinessException;
import com.raktim.fiverclone.common.utils.PaginationUtil;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponseDTO createUser(UserDTO userDTO) {
        log.info("Creating user {}", userDTO);
        UserEntity foundUser = userRepo.findByEmail(userDTO.email());
        validateUserForCreation(foundUser);

        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity.setPassword(passwordEncoder.encode(userDTO.password())); // encode the password

        UserEntity newUser =  userRepo.save(userEntity);
        return userMapper.toDetailResponseDTO(newUser);
    }

    @Override
    public UserResponseDTO findUserById(UUID id) {
        UserEntity userEntity = findUserByIdOrThrow(id);
        return userMapper.toDetailResponseDTO(userEntity);
    }

    @Override
    public PaginatedResponseDto<UserListResponseDto> findAllUsers(Integer pageNumber, Integer pageSize) {
        log.info("Fetching all the users from DB.");
        Page<UserEntity> paginatedUsers = userRepo.findAll(PageRequest.of(pageNumber, pageSize));
        return PaginationUtil.build(paginatedUsers, userMapper::toListResponseDTO);
    }

    private @NonNull UserEntity findUserByIdOrThrow(UUID id) {
        log.info("Finding user by id {}", id);
        UserEntity userEntity = userRepo.findById(id).orElse(null);
        if (userEntity == null) {
            throw new BusinessException(
                    HttpStatus.NOT_FOUND,
                    "USER_NOT_FOUND",
                    "User with id " + id + " not found"
            );
        }
        return userEntity;
    }

    private void validateUserForCreation(UserEntity user) {
        if (user != null) {
            throw new BusinessException(
                    HttpStatus.CONFLICT,
                    "USER_ALREADY_EXISTS",
                    user.getEmail() +
                            " email already exists"
            );
        }
    }
}
