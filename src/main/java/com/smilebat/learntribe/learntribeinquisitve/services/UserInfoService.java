package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.SkillRequest;
import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.WorkExperienceRequest;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.SkillConverter;
import com.smilebat.learntribe.learntribeinquisitve.converters.UserProfileConverter;
import com.smilebat.learntribe.learntribeinquisitve.converters.WorkExperienceConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.SkillRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.WorkExperienceRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.WorkExperience;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class related user business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {

  private final UserDetailsRepository userDetailsRepository;
  private final WorkExperienceRepository workExperienceRepository;
  private final SkillRepository skillRepository;
  private final UserProfileConverter profileConverter;
  private final SkillConverter skillConverter;
  private final WorkExperienceConverter workExperienceConverter;

  /**
   * Saves all user information.
   *
   * @param profileRequest the {@link UserProfileRequest}.
   * @return the {@link UserProfileRequest}.
   */
  @Transactional
  public Long saveUserInfo(UserProfileRequest profileRequest) {
    Verify.verifyNotNull(profileRequest, "User Profile Request Cannot be Null");
    List<SkillRequest> skillsRequest = profileRequest.getSkills();
    List<WorkExperienceRequest> workExperienceRequests = profileRequest.getWorkExperiences();
    UserProfile userProfile = saveUserProfile(profileRequest);
    saveUserSkills(userProfile, skillsRequest);
    saveWorkExperiences(userProfile, workExperienceRequests);
    return userProfile.getId();
  }

  /**
   * Retrieves all the user profile details.
   *
   * @param userId the {@link String} user id
   * @return the {@link UserProfileResponse}
   */
  public UserProfileResponse getUserInfo(String userId) {
    Verify.verifyNotNull(userId, "User Id cannot be null");
    UserProfile userProfile = userDetailsRepository.findByKeyCloakId(userId);
    return profileConverter.toResponse(userProfile);
  }

  /**
   * Saves all the user profile details.
   *
   * @param profileRequest the {@link UserProfileRequest}
   * @return the {@link UserProfile}
   */
  private UserProfile saveUserProfile(UserProfileRequest profileRequest) {
    String keycloakId = profileRequest.getKeyCloakId();
    UserProfile userProfile = userDetailsRepository.findByKeyCloakId(keycloakId);
    if (userProfile == null) {
      userProfile = profileConverter.toEntity(profileRequest);
      userDetailsRepository.save(userProfile);
    }
    return userProfile;
  }

  /**
   * Saves all the user skills.
   *
   * @param profile the {@link UserProfile}
   * @param skillsRequest the List of {@link SkillRequest}
   * @return the List of {@link Skill}
   */
  private Collection<Skill> saveUserSkills(UserProfile profile, List<SkillRequest> skillsRequest) {
    if (skillsRequest == null || skillsRequest.isEmpty()) {
      return Collections.emptyList();
    }
    Set<Skill> existingSkills = profile.getSkills();
    Set<Skill> requestSkills = skillConverter.toEntities(skillsRequest,profile);
    Set<Skill> updatedSkills = new HashSet<>();
    updatedSkills.addAll(requestSkills);
    updatedSkills.addAll(existingSkills);
    skillRepository.saveAll(updatedSkills);
    return updatedSkills;
  }

  /**
   * Saves all user work experiences.
   *
   * @param profile the {@link UserProfile}
   * @param request the List of {@link WorkExperienceRequest}
   * @return the List of {@link WorkExperience}
   */
  private Collection<WorkExperience> saveWorkExperiences(
      UserProfile profile, List<WorkExperienceRequest> request) {
    if (request == null || request.isEmpty()) {
      return Collections.emptyList();
    }
    Set<WorkExperience> existingExperiences = profile.getWorkExperiences();
    Set<WorkExperience> workExperiences = workExperienceConverter.toEntities(request, profile);
    Set<WorkExperience> updatedWorkExperiences = new HashSet<>();
    updatedWorkExperiences.addAll(workExperiences);
    updatedWorkExperiences.addAll(existingExperiences);
    workExperienceRepository.saveAll(updatedWorkExperiences);
    return updatedWorkExperiences;
  }
}
