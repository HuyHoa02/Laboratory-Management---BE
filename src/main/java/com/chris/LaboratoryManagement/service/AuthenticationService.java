package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.mail.DataMailDTO;
import com.chris.LaboratoryManagement.dto.request.*;
import com.chris.LaboratoryManagement.dto.response.AuthenticationResponse;
import com.chris.LaboratoryManagement.dto.response.IntrospectResponse;
import com.chris.LaboratoryManagement.dto.response.UserResponse;
import com.chris.LaboratoryManagement.entity.Role;
import com.chris.LaboratoryManagement.entity.Token;
import com.chris.LaboratoryManagement.entity.User;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.enums.RoleEnum;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.UserMapper;
import com.chris.LaboratoryManagement.repository.RoleRepository;
import com.chris.LaboratoryManagement.repository.TokenRepository;
import com.chris.LaboratoryManagement.repository.UserRepository;
import com.chris.LaboratoryManagement.utils.Helper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    TokenRepository tokenRepository;
    Helper helper;
    MailService mailService;
    UserMapper userMapper;


    private final RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected  String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected  long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected  long REFRESH_DURATION;

    public AuthenticationResponse login(AuthenticationRequest request){
        log.info(request.getUsername());

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        var authenticatedResult = true;
        if(!user.getVerified()) authenticatedResult = false;

        return AuthenticationResponse.builder()
                .token(token)
                .id(user.getId())
                .authenticated(authenticatedResult)
                .build();
    }

    public String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("e_commerce.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.MINUTES).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw  new AppException(ErrorCode.TOKEN_UNGENERATED);
        }
    }

    private  String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
            });
        return  stringJoiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e){
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signedToken = verifyToken(request.getToken(), true);

            String jit = signedToken.getJWTClaimsSet().getJWTID();
            log.info(jit);
            Token invalidateToken = Token.builder()
                    .id(jit)
                    .build();

            tokenRepository.save(invalidateToken);
        }catch (AppException e) {
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getExpirationTime()
                .toInstant()
                .plus(REFRESH_DURATION, ChronoUnit.MINUTES)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        // If the token is not verified or the expiration time is in the past, throw the token expired exception
        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.TOKEN_EXPIRED);  // <-- This triggers the Token Expired Exception

        if(tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }


    public AuthenticationResponse refreshToken(RefreshRequest request)
            throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        Token invalidateToken = Token.builder()
                .id(jit)
                .build();

        tokenRepository.save(invalidateToken);

        var username = signJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    public UserResponse signup(UserCreationRequest request, RoleEnum role) throws MessagingException {
        var usernameChecker = userRepository.findByUsername(request.getUsername()).orElse(null);
        if(usernameChecker != null )
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        String verifyCode = helper.generateTempPwd(6);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(role.name()).ifPresent(roles::add);

        user.setRoles(roles);
        user.setCreatedAt(LocalDate.now());
        user.setVerificationCode(verifyCode);
        user.setVerificationExpiry(LocalDateTime.now().plusHours(24));//thoi gian het han code 24h
        user.setVerified(false);


        user = userRepository.save(user);

        Map<String,Object> props = new HashMap<>();
        props.put("firstName",request.getFirstname());
        props.put("lastName",request.getLastname());
        props.put("code",verifyCode);

        DataMailDTO dataMailDTO = DataMailDTO.builder()
                .subject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG")
                .to(request.getEmail())
                .props(props)
                .build();
        mailService.sendVerificationMail(dataMailDTO);



        return userMapper.toUserResponse(user);
    }

    public AuthenticationResponse verifyEmail(EmailVerifyRequest request){
        User userChecker = userRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTEXISTED));
        if(!Objects.equals(userChecker.getVerificationCode(), request.getVerifyCode())){
            log.info("In database: "+userChecker.getVerificationCode()+" Requset: "+ request.getVerifyCode());
            throw new AppException(ErrorCode.VERIFY_EMAIL_FAILED);
        }

        userChecker.setVerified(true);
        userChecker.setVerificationExpiry(null);
        userChecker.setVerificationCode(null);

        userRepository.save(userChecker);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .id(request.getLecturerId())
                .build();
    }
}
