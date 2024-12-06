package com.chris.LaboratoryManagement.mapper;

import com.chris.LaboratoryManagement.dto.request.AgendaCreationRequest;
import com.chris.LaboratoryManagement.dto.request.CourseCreationRequest;
import com.chris.LaboratoryManagement.dto.response.AgendaCreationResponse;
import com.chris.LaboratoryManagement.dto.response.CourseResponse;
import com.chris.LaboratoryManagement.entity.Agenda;
import com.chris.LaboratoryManagement.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AgendaMapper {
    Agenda toAgenda(AgendaCreationRequest request);
    AgendaCreationResponse toAgendaResponse(Agenda agenda);
}
