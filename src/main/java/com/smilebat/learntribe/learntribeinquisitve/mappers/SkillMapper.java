package com.smilebat.learntribe.learntribeinquisitve.mappers;

import com.smilebat.learntribe.inquisitive.request.SkillDTO;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.Skill;
import org.mapstruct.*;

/** Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}. */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {}
