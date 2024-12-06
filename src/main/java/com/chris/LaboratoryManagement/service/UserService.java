package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.mail.DataMailDTO;
import com.chris.LaboratoryManagement.dto.request.UpdatePasswordRequest;
import com.chris.LaboratoryManagement.dto.request.UserCreationRequest;
import com.chris.LaboratoryManagement.dto.request.UserUpdateRequest;
import com.chris.LaboratoryManagement.dto.response.UserResponse;
import com.chris.LaboratoryManagement.entity.Role;
import com.chris.LaboratoryManagement.entity.User;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.enums.RoleEnum;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.UserMapper;
import com.chris.LaboratoryManagement.repository.RoleRepository;
import com.chris.LaboratoryManagement.repository.UserRepository;
import com.chris.LaboratoryManagement.utils.Helper;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    Helper helper;
    private final RoleRepository roleRepository;
    MailService mailService;
    PasswordEncoder passwordEncoder;

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
public List<UserResponse> getAllLecturers() {
    return userRepository.findAll().stream()
            .filter(user -> !"admin".equals(user.getId())) // Filter out the user with ID "admin"
            .map(userMapper::toUserResponse)
            .toList(); // Use collect instead of toList() for compatibility with older versions of Java
}


    public UserResponse createLecturer(UserCreationRequest request) throws MessagingException {
        // Check whether new user's email had existed in database
        Optional<User> emailChecker = userRepository.findByEmail(request.getEmail());
        if(emailChecker.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        // Check whether new user's username had existed in database
        Optional<User> usernameChecker = userRepository.findByUsername(request.getUsername());
        if(usernameChecker.isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // Map all information in the request to a specific user entity
        User newUser = userMapper.toUser(request);

        // Because the lecturer's id must follow a rule example: C000001, C000002,...
        // So this function being created to select the latest user id and generate a new id follow the rule
        String newId = helper.generateNextUserId();
        log.info("Generated ID: {}", newId);
        newUser.setId(newId);

        newUser.setCreatedAt(LocalDate.now());

        //Generate a random 6 letters code for lecturer to verify email
        String verifyCode = helper.generateTempPwd(6);


        //Set the new lecturer's role to LECTURER_ROLE
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName(RoleEnum.LECTURER).ifPresent(roles::add);
        newUser.setRoles(roles);

        // These code for lecturer verifying email process
        newUser.setVerificationCode(verifyCode);
        newUser.setVerificationExpiry(LocalDateTime.now().plusHours(24));//thoi gian het han code 24h
        newUser.setVerified(false);

        // Encoded lecturer password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        //Send Verifier Email
        Map<String,Object> props = new HashMap<>();
        props.put("firstName",request.getFirstname());
        props.put("lastName",request.getLastname());
        props.put("code",verifyCode);
        props.put("id",newUser.getId());

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .subject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG")
                .to(request.getEmail())
                .props(props)
                .build();
        mailService.sendVerificationMail(dataMailDTO);

        userRepository.save(newUser);

        return userMapper.toUserResponse(newUser);
    }

    public UserResponse getMyInfo(String id) {
        Optional<User> userResult = userRepository.findById(id);
        if(userResult.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOTEXISTED);
        }
        return userMapper.toUserResponse(userResult.get());
    }

    public UserResponse updateInfo(UserUpdateRequest request, String id){
        User userResult = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        userResult.setFirstname(request.getFirstname());
        userResult.setLastname(request.getLastname());
        userResult.setGender(request.getGender());
        userResult.setEmail(request.getEmail());
        userResult.setPhoneNum(request.getPhoneNum());

        User savedUser = userRepository.save(userResult);

        return userMapper.toUserResponse(savedUser);
    }

    public String deleteLecturer(String id){
        Optional<User> userChecker = userRepository.findById(id);
        if(userChecker.isEmpty()){
            throw new AppException(ErrorCode.USER_NOTEXISTED);
        }

        userRepository.deleteById(id);
        return "Lecturer has been deleted successfully";
    }

    public String updatePassword(UpdatePasswordRequest request){
        User userChecker = userRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userChecker.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(userChecker);

        return "Updated password succeed";
    }
}
