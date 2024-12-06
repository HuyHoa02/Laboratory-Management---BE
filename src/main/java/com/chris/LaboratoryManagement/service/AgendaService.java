package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.AgendaCreationRequest;
import com.chris.LaboratoryManagement.dto.request.ClassCreationRequest;
import com.chris.LaboratoryManagement.dto.request.SearchingRequest;
import com.chris.LaboratoryManagement.dto.response.AgendaCreationResponse;
import com.chris.LaboratoryManagement.dto.response.ApiResponse;
import com.chris.LaboratoryManagement.dto.response.ClassCreationResponse;
import com.chris.LaboratoryManagement.entity.*;
import com.chris.LaboratoryManagement.entity.Class;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.enums.StateEnum;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.AgendaMapper;
import com.chris.LaboratoryManagement.repository.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    ClassRepository classRepository;
    LaboratoryRepository laboratoryRepository;
    SessionRepository sessionRepository;
    TimeSetRepository timeSetRepository;
    RegistrationRepository registrationRepository;
    AgendaMapper agendaMapper;

    public AgendaCreationResponse addAgenda(AgendaCreationRequest request) {

        // Fetch the Registration entity, throw exception if not found
        var registrationChecker = registrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new AppException(ErrorCode.REGISTRATION_NOT_EXISTED));

        if (registrationChecker.getState().equals(StateEnum.ACEPTED)
                || registrationChecker.getState().equals(StateEnum.DENIED)) {
            throw new AppException(ErrorCode.REGISTRATION_HANDLED);
        }

        // Fetch the TimeSet based on reservationDate, throw exception if not found
        var timsetIdChecker = timeSetRepository.findByReservationDate(registrationChecker.getReservationDate())
                .orElseThrow(() -> new AppException(ErrorCode.TIMESET_NOT_EXISTED));

        // Fetch the laboratory associated with the registration
        Optional<Laboratory> laboratory = laboratoryRepository.findById(registrationChecker.getLaboratory().getId());

        // If LabId is provided, override the laboratory selection
        if (request.getLaboratoryId().isPresent()) {
            laboratory = laboratoryRepository.findById(request.getLaboratoryId().get());
        }

        // If the laboratory is still not found, throw an exception
        if (laboratory.isEmpty()) {
            throw new AppException(ErrorCode.LABORATORY_NOT_EXISTED);
        }

        registrationChecker.setState(StateEnum.ACEPTED);

        // Build and save the Agenda entity
        Agenda agenda = Agenda.builder()
                .registration(registrationChecker)
                .available(false) // Set as unavailable initially
                .date(registrationChecker.getReservationDate())
                .aClass(registrationChecker.getAClass())
                .laboratory(laboratory.get()) // Set the found or provided laboratory
                .session(registrationChecker.getSession())
                .timeSet(timsetIdChecker)
                .build();

        agendaRepository.save(agenda);

        // Return the response with the saved agenda details
        return AgendaCreationResponse.builder()
                .available(false)
                .date(registrationChecker.getReservationDate())
                .labId(laboratory.get().getId()) // Correctly reflect the selected laboratory
                .sessionName(registrationChecker.getSession().getName())
                .classId(agenda.getAClass().getId())
                .courseName(agenda.getAClass().getCourse().getName())
                .lecturerName(agenda.getAClass().getUser().getFirstname()+" "+agenda.getAClass().getUser().getLastname())
                .build();
    }

    public List<AgendaCreationResponse> getAgendaByCurrentTimeSet(int index) {
        String timeSetId = timeSetRepository.findIdsByIsCurrent(index);
        if(timeSetId.isEmpty()){
            throw new AppException(ErrorCode.TIMESET_NOT_EXISTED);
        }

        return agendaRepository.findByTimeSetId(timeSetId).stream().map(agenda -> {
            return AgendaCreationResponse.builder()
                    .date(agenda.getDate())
                    .labId(agenda.getLaboratory().getId())
                    .sessionName(agenda.getSession().getName())
                    .classId(agenda.getAClass().getId())
                    .courseName(agenda.getAClass().getCourse().getName())
                    .lecturerName(agenda.getAClass().getUser().getFirstname()+" "+agenda.getAClass().getUser().getLastname())
                    .available(false)
                    .build();
        }).toList();
    }

    public List<AgendaCreationResponse> getAgendaByInput(SearchingRequest request) {
        List<Agenda> agendaList;
        if(request.getInput().isEmpty()) return new ArrayList<>();

        switch (request.getMethod()) {
            case "courseName":
                agendaList = agendaRepository.findByCourseNameContaining(request.getInput());
                break;
            case "classId":
                agendaList = agendaRepository.findByClassIdContaining(request.getInput());
                break;
            case "lecturerName":
                // Split the input into firstname and lastname
                String lecturerName = request.getInput();
                agendaList = agendaRepository.findByLecturerNameContaining(lecturerName);
                break;
            default:
                agendaList = new ArrayList<>();
        }

        return agendaList.stream()
                .map(agenda -> AgendaCreationResponse.builder()
                        .date(agenda.getDate())
                        .labId(agenda.getLaboratory().getId())
                        .sessionName(agenda.getSession().getName())
                        .classId(agenda.getAClass().getId())
                        .courseName(agenda.getAClass().getCourse().getName())
                        .lecturerName(agenda.getAClass().getUser().getFirstname() + " " + agenda.getAClass().getUser().getLastname())
                        .available(agenda.isAvailable())
                        .build())
                .collect(Collectors.toList());
    }


}
