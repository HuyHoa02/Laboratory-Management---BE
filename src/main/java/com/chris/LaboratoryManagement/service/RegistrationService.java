package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.request.AgendaCreationRequest;
import com.chris.LaboratoryManagement.dto.request.RegistrationCreationRequest;
import com.chris.LaboratoryManagement.dto.response.RegistrationCreationResponse;
import com.chris.LaboratoryManagement.entity.Registration;
import com.chris.LaboratoryManagement.enums.ErrorCode;
import com.chris.LaboratoryManagement.enums.StateEnum;
import com.chris.LaboratoryManagement.exception.AppException;
import com.chris.LaboratoryManagement.mapper.RegistrationMapper;
import com.chris.LaboratoryManagement.repository.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RegistrationService {
    private final AgendaRepository agendaRepository;
    ClassRepository classRepository;
    UserRepository userRepository;
    LaboratoryRepository laboratoryRepository;
    SessionRepository sessionRepository;
    RoleRepository roleRepository;
    RegistrationRepository registrationRepository;
    RegistrationMapper registrationMapper;
    TimeSetRepository timeSetRepository;

    public RegistrationCreationResponse addRegistration(RegistrationCreationRequest request){
        var classChecker = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_EXISTED));

        var laboratoryChecker = laboratoryRepository.findById(request.getLabId())
                .orElseThrow(() -> new AppException(ErrorCode.LABORATORY_NOT_EXISTED));

        var sessionChecker = sessionRepository.findByName(request.getSessionName())
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_NOT_EXISTED));

        boolean isDateWithinCurrentTimeSet = timeSetRepository.existsByReservationDateWithinCurrentTimeSet(request.getReservationDate());


        if (!isDateWithinCurrentTimeSet || request.getReservationDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.INVALID_RESERVATION_DATE);
        }

        boolean agendaExists = agendaRepository.existsByLaboratoryAndSessionAndDate(laboratoryChecker, sessionChecker, request.getReservationDate());

        if (agendaExists) {
            throw new AppException(ErrorCode.AGENDA_ALREADY_EXISTS);
        }

        Registration registration = Registration.builder()
                .createdAt(Timestamp.from(Instant.now()))
                .state(StateEnum.PENDING)
                .aClass(classChecker)
                .laboratory(laboratoryChecker)
                .reservationDate(request.getReservationDate())
                .session(sessionChecker)
                .build();

        registrationRepository.save(registration);

        return RegistrationCreationResponse.builder()
                .classId(registration.getAClass().getId())
                .sessionName(registration.getSession().getName())
                .reservationDate(request.getReservationDate())
                .state(registration.getState())
                .createdAt(registration.getCreatedAt())
                .labId(registration.getLaboratory().getId())
                .build();
    }

    public List<RegistrationCreationResponse> getAll(){
        return registrationRepository.findAllAscOfState()
                .stream()
                .map(registration -> {
                    var registrationResponse = registrationMapper.toRegistrationResponse(registration);
                    registrationResponse.setRegistrationId(registration.getId());
                    registrationResponse.setClassId(registration.getAClass().getId());
                    registrationResponse.setLabId(registration.getLaboratory().getId());
                    registrationResponse.setSessionName(registration.getSession().getName());
                    return registrationResponse;
                })
                .toList();

    }

    public String reject(AgendaCreationRequest request){
        var registrationChecker = registrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new AppException(ErrorCode.REGISTRATION_NOT_EXISTED));

        if (registrationChecker.getState().equals(StateEnum.ACEPTED)
                || registrationChecker.getState().equals(StateEnum.DENIED)) {
            throw new AppException(ErrorCode.REGISTRATION_HANDLED);
        }

        registrationChecker.setState(StateEnum.DENIED);

        registrationRepository.save(registrationChecker);

        return "Registration have been denied!";
    }


    public List<RegistrationCreationResponse> readExcel(MultipartFile file) {
        List<RegistrationCreationRequest> registrationRequests = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Read the first sheet

            log.info(String.valueOf(sheet.getLastRowNum()));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Logging the cell value for debugging
                log.info(String.valueOf(row == null));
                log.info(row.getCell(0).getStringCellValue());
                log.info(row.getCell(1).getStringCellValue());
                log.info(row.getCell(2).getStringCellValue());
                log.info(sdf.format(row.getCell(3).getDateCellValue()));

                // Handle each cell with a check to avoid null values
                RegistrationCreationRequest request = RegistrationCreationRequest.builder()
                        .classId(row.getCell(0).getStringCellValue())
                        .labId(row.getCell(1).getStringCellValue())
                        .sessionName(row.getCell(2).getStringCellValue())
                        .reservationDate(LocalDate.from(row.getCell(3).getLocalDateTimeCellValue()))
                        .build();

                registrationRequests.add(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage(), e);
        }

        // Assuming addRegistration(request) processes each request and returns a response
        return registrationRequests.stream()
                .map(request -> addRegistration(request))
                .toList();
    }


    public void logCellValue(Row row) {
        Cell cell = row.getCell(3); // Get the cell at index 3 (reservationDate)

        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    log.info("TIO1: " + cell.getStringCellValue());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // If it's a date, format it as a string
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                        log.info("TIO2: " + sdf.format(cell.getDateCellValue()));
                    } else {
                        log.info("TIO3: " + cell.getNumericCellValue());
                    }
                    break;
                default:
                    log.info("TIO: Unknown cell type");
            }
        } else {
            log.info("TIO: Cell is null");
        }
    }
}
