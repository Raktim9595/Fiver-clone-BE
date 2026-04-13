package com.raktim.fiverclone.user.service;

import com.raktim.fiverclone.common.DTO.PaginatedResponseDto;
import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.utils.UserMapper;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        userService = new UserServiceImpl(userRepo, userMapper, passwordEncoder);
    }

    @Test
    public void createUser_shouldCreateUserSuccessfully() {
        UUID id = UUID.randomUUID();
        UserDTO userDto = UserTestDataFactory.validUserDto().build();
        UserEntity userEntity = UserTestDataFactory
                .validUserEntity()
                .build();
        userEntity.setId(id);

        when(userRepo.findByEmail(userDto.email())).thenReturn(null);
        when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponseDTO result = userService.createUser(userDto);

        assertNotNull(result);
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepo).save(captor.capture());
        verify(userRepo).findByEmail(userDto.email());

        assertAll(
                () -> assertInstanceOf(UserResponseDTO.class, result),
                () -> assertEquals(id, result.id()),
                () -> assertEquals(userDto.email(), result.email()),
                () -> assertEquals(userDto.username(), result.username()),
                () -> assertEquals(userDto.address(), result.address()),
                () -> assertEquals(result.phoneNumber(), userDto.phoneNumber())
        );
    }

    @Test
    public void createUser_shouldThrowException_finByEmailIsNotNull() {
        UUID id = UUID.randomUUID();
        UserDTO userDto = UserTestDataFactory.validUserDto().build();
        UserEntity userEntity = UserTestDataFactory.validUserEntity().build();
        userEntity.setId(id);

        when(userRepo.findByEmail(anyString())).thenReturn(userEntity);


        ExceptionTestUtil.assertBusinessException(
                HttpStatus.CONFLICT,
                "USER_ALREADY_EXISTS",
                userDto.email() + " email already exists",
                () -> userService.createUser(userDto)
        );

        verify(userRepo, never()).save(any(UserEntity.class));
        verify(userRepo).findByEmail(userDto.email());
    }

    @Test
    public void findUserById_shouldReturnUserSuccessfully() {
        UUID id = UUID.randomUUID();
        UserEntity userEntity = UserTestDataFactory.validUserEntity().build();
        userEntity.setId(id);

        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));
        UserResponseDTO result = userService.findUserById(id);

        assertNotNull(result);
        assertInstanceOf(UserResponseDTO.class, result);
        assertEquals(id, result.id());
    }

    @Test
    public void findUserById_shouldThrowException_finByEmailIsNull() {
        UUID id = UUID.randomUUID();
        UserEntity userEntity = UserTestDataFactory.validUserEntity().build();
        userEntity.setId(id);

        when(userRepo.findById(id)).thenReturn(Optional.empty());
        ExceptionTestUtil.assertBusinessException(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                "User with id " + id + " not found",
                () -> userService.findUserById(id)
        );

        verify(userRepo).findById(id);
    }

    @Test
    public void findAllUsers_shouldReturnAllUsers() {
        // Arrange
        int pageNumber = 0;
        int pageSize = 1;

        UserEntity user1 = UserTestDataFactory.validUserEntity().build();
        user1.setId(UUID.randomUUID());
        user1.setUsername("Raktim");
        user1.setEmail("raktim@gmail.com");

        UserEntity user2 = UserTestDataFactory.validUserEntity().build();
        user2.setId(UUID.randomUUID());
        user2.setUsername("Alex");
        user2.setEmail("alex@gmail.com");

        Page<UserEntity> userPage = new PageImpl<>(
                List.of(user1, user2),
                PageRequest.of(pageNumber, pageSize),
                2
        );

        when(userRepo.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(userPage);
        PaginatedResponseDto<UserListResponseDto> result =
                userService.findAllUsers(pageNumber, pageSize);

        assertNotNull(result);
        assertInstanceOf(PaginatedResponseDto.class, result);
        assertNotNull(result.items());
        assertAll(
                () -> assertEquals(2, result.items().size()),
                () -> assertEquals(pageNumber, result.page()),
                () -> assertEquals(pageSize, result.pageSize()),
                () -> assertEquals(2, result.total()),
                () -> assertEquals(2, result.totalPages())
        );

        verify(userRepo).findAll(PageRequest.of(pageNumber, pageSize));
        assertInstanceOf(PaginatedResponseDto.class, result);
        System.out.println(result.items());

        UserListResponseDto firstUser = result.items().getFirst();
        assertInstanceOf(UserListResponseDto.class, firstUser);
    }

    @Test
    @DisplayName("When called findUserByIdOrThrow, And repo returns empty, Then it should throw exception")
    public void testFindByIdOrThrow() {
        UUID id = UUID.randomUUID();
        when(userRepo.findById(id)).thenReturn(Optional.empty());
        ExceptionTestUtil.assertBusinessException(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                "User with id " + id + " not found",
                () -> userService.findUserByIdOrThrow(id)
        );

        verify(userRepo).findById(id);
    }

    @Test
    @DisplayName("When called findUserByIdOrThrow, And repo returns valid entity, Then it should return entity")
    public void testFindByIdOrThrow_validEntity() {
        UUID id = UUID.randomUUID();
        UserEntity userEntity = UserTestDataFactory.validUserEntity().build();
        userEntity.setId(id);
        when(userRepo.findById(id)).thenReturn(Optional.of(userEntity));
        UserEntity result = userService.findUserByIdOrThrow(id);
        assertNotNull(result);
        assertInstanceOf(UserEntity.class, result);
        assertEquals(id, result.getId());
    }
}
