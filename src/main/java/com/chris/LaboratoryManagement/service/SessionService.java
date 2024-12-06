package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.SchoolYearCreationRequest;
import com.chris.LaboratoryManagement.dto.request.SessionCreationRequest;
import com.chris.LaboratoryManagement.dto.response.SchoolYearCreationResponse;
import com.chris.LaboratoryManagement.dto.response.SessionCreationResponse;
import com.chris.LaboratoryManagement.entity.SchoolYear;
import com.chris.LaboratoryManagement.entity.Session;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.repository.SessionRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SessionService {
    SessionRepository sessionRepository;

    public SessionCreationResponse addSession(SessionCreationRequest request){
        sessionRepository.findByName(request.getName()).ifPresent(session -> {
            throw new AppException(ErrorCode.SESSION_EXISTED);
        });

        Session ses = Session.builder()
                .name(request.getName())
                .build();
        sessionRepository.save(ses);
        return SessionCreationResponse.builder()
                .name(ses.getName())
                .build();
    }

    public List<SessionCreationResponse> getAll(){
        return sessionRepository.findAll().stream().map(session -> {
            return SessionCreationResponse.builder()
                    .name(session.getName())
                    .build();
        }).toList();
    }
}
