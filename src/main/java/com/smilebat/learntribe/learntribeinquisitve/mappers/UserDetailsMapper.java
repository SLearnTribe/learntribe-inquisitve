package com.smilebat.learntribe.learntribeinquisitve.mappers;

import com.smilebat.learntribe.inquisitive.request.UserProfileDTO;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.UserDetails;
import org.mapstruct.*;

/** Mapper for the entity {@link UserDetails} and its DTO {@link UserProfileDTO}. */
@Mapper(componentModel = "spring")
public interface UserDetailsMapper extends EntityMapper<UserProfileDTO, UserDetails> {}
