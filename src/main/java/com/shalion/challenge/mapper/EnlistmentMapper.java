package com.shalion.challenge.mapper;

import com.shalion.challenge.dto.EnlistmentAcceptedResponse;
import com.shalion.challenge.dto.EnlistmentStatusResponse;
import com.shalion.challenge.domain.EnlistmentProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnlistmentMapper {

    /**
     * Maps an enlistment process to the accepted response payload.
     *
     * @param process enlistment process entity
     * @return accepted response
     */
    @Mapping(target = "processId", source = "id")
    EnlistmentAcceptedResponse toAcceptedResponse(EnlistmentProcess process);

    /**
     * Maps an enlistment process to the status response payload.
     *
     * @param process enlistment process entity
     * @return status response
     */
    @Mapping(target = "processId", source = "id")
    @Mapping(target = "finished", source = "finished")
    EnlistmentStatusResponse toStatusResponse(EnlistmentProcess process);
}
