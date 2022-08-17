package com.smilebat.learntribe.learntribeinquisitve.mappers;

import com.smilebat.learntribe.inquisitive.request.WorkExperienceDTO;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.WorkExperience;
import org.mapstruct.*;

/** Mapper for the entity {@link WorkExperience} and its DTO {@link WorkExperienceDTO}. */
@Mapper(componentModel = "spring")
public interface WorkExperienceMapper extends EntityMapper<WorkExperienceDTO, WorkExperience> {}
