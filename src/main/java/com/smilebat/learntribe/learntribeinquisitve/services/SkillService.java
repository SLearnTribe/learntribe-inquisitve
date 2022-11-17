package com.smilebat.learntribe.learntribeinquisitve.services;

import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.UserProfileConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.SkillRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Skill Service to hold the business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Likith
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {
  private final SkillRepository skillRepository;
  private final UserProfileConverter profileConverter;

  /**
   * Retrieves the List of {@link UserProfileResponse}.
   *
   * @param skillName the List of {@link String}.
   * @return the List of {@link UserProfileResponse}.
   */
  public List getUserInfo(String skillName) {
    log.info("Finding user list based on the skill");
    List<Skill> userSkills = skillRepository.findBySkillName(skillName);

    if (userSkills == null || userSkills.isEmpty()) {
      return Collections.emptyList();
    }

    List<UserProfile> profiles = retreveUserProfiles(userSkills);

    return profileConverter.toResponse(profiles);
  }

  private List<UserProfile> retreveUserProfiles(Collection<Skill> userSkills) {
    return userSkills
        .stream()
        .filter(this::hasUserDetails)
        .map(Skill::getUserDetails)
        .collect(Collectors.toList());
  }

  private boolean hasUserDetails(Skill skill) {
    return skill.getUserDetails() != null;
  }
}
