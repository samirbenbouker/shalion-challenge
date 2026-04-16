package com.shalion.challenge.mapper;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.domain.EnlistmentProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnlistmentMapper {

    @Mapping(target = "processId", source = "id")
    EnlistmentAcceptedResponse toAcceptedResponse(EnlistmentProcess process);

    @Mapping(target = "processId", source = "id")
    @Mapping(target = "finished", source = "finished")
    EnlistmentStatusResponse toStatusResponse(EnlistmentProcess process);
}
