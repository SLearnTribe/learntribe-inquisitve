package com.smilebat.learntribe.learntribeinquisitve.services;

import com.smilebat.learntribe.inquisitive.request.SkillDTO;
import com.smilebat.learntribe.inquisitive.request.UserProfileDTO;
import com.smilebat.learntribe.inquisitive.request.WorkExperienceDTO;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.UserDetails;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.WorkExperience;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.repository.SkillRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.repository.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.repository.WorkExperienceRepository;
import com.smilebat.learntribe.learntribeinquisitve.mappers.SkillMapper;
import com.smilebat.learntribe.learntribeinquisitve.mappers.UserDetailsMapper;
import com.smilebat.learntribe.learntribeinquisitve.mappers.WorkExperienceMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoServices {

  private final UserDetailsRepository userDetailsRepository;
  private final WorkExperienceRepository workExperienceRepository;
  private final SkillRepository skillRepository;

  @Autowired private UserDetailsMapper userDetailsMapper;

  @Autowired private SkillMapper skillMapper;

  @Autowired private WorkExperienceMapper workExperienceMapper;

  @Transactional
  public UserProfileDTO SaveUserInfo(UserProfileDTO profileRequest) {

    UserDetails details = userDetailsMapper.toEntity(profileRequest);
    details = userDetailsRepository.save(details);

    List<Skill> allDataSkil = skillRepository.findAll();
    List<Skill> allSkill = skillMapper.toEntity(profileRequest.getSkills());

    UserDetails finalDetails = details;
    List<Skill> updatedSkillData =
        allSkill
            .stream()
            .map(
                skill -> {
                  skill.setUserDetails(finalDetails);
                  return skill;
                })
            .collect(Collectors.toList());
    List<Skill> savedSkill = skillRepository.saveAll(updatedSkillData);

    details.setSkills(new HashSet<>(savedSkill));

    Set<WorkExperience> allWorkExpSaved = new HashSet<>();

    if (profileRequest.getWorkExperience() != null) {
      List<WorkExperience> workExpnc =
          profileRequest
              .getWorkExperience()
              .stream()
              .map(
                  wrk -> {
                    WorkExperience experience = new WorkExperience();
                    BeanUtils.copyProperties(wrk, experience);
                    return experience;
                  })
              .collect(Collectors.toList());

      workExpnc = workExperienceRepository.saveAll(workExpnc);
      allWorkExpSaved = new HashSet<>(workExpnc);
      details.setWorkExperiences(allWorkExpSaved);
    }

    UserDetails detailsUser = userDetailsRepository.save(details);

    return converttoDTO(detailsUser);
  }

  /**
   * Method to Convert Response to DTO
   *
   * @param detailsUser
   * @return
   */
  private UserProfileDTO converttoDTO(UserDetails detailsUser) {
    UserProfileDTO returnData = userDetailsMapper.toDto(detailsUser);

    if (detailsUser != null && detailsUser.getSkills() != null) {
      List<SkillDTO> skillDTO = skillMapper.toDto(new ArrayList<>(detailsUser.getSkills()));
      returnData.setSkills(skillDTO);
    }
    if (detailsUser != null && detailsUser.getWorkExperiences() != null) {
      List<WorkExperienceDTO> workExperienceDTOList =
          workExperienceMapper.toDto(new ArrayList<>(detailsUser.getWorkExperiences()));
      returnData.setWorkExperience(workExperienceDTOList);
    }

    return returnData;
  }
}
