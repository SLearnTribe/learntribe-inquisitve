package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.WorkExperienceRequest;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.UserProfileConverter;
import com.smilebat.learntribe.learntribeinquisitve.converters.WorkExperienceConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserProfileSearchRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.WorkExperienceRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  private final UserProfileSearchRepository userProfileSearchRepository;
  private final WorkExperienceRepository workExperienceRepository;
  private final UserProfileConverter profileConverter;
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
    UserProfile userProfile = saveUserProfile(profileRequest);
    return userProfile.getId();
  }

  /**
   * Retrieves all the user profile details based on id.
   *
   * @param userId the {@link String} user id
   * @return the {@link UserProfileResponse}
   */
  @Transactional
  public UserProfileResponse getUserInfo(String userId) {
    Verify.verifyNotNull(userId, "User Id cannot be null");
    UserProfile userProfile = userDetailsRepository.findByKeyCloakId(userId);
    if (userProfile == null) {
      return new UserProfileResponse();
    }
    return profileConverter.toResponse(userProfile);
  }

  /**
   * Retrieves all the user profile based on skill.
   *
   * @param skill skill necessary in the candidate.
   * @param pageNo page number for pageination
   * @param pageSize for pageination
   * @return the {@link UserProfileResponse}
   */
  @Transactional
  public List<UserProfileResponse> getUserInfoBySkill(String skill, int pageNo, int pageSize) {
    Verify.verifyNotNull(skill, "Skill cannot be empty");
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
    List<UserProfile> userProfile = userDetailsRepository.findBySkills(skill, pageable);
    if (userProfile == null) {
      return Collections.emptyList();
    }

    return profileConverter.toResponse(userProfile);
  }

  /**
   * Retrieves all the user profile details.
   *
   * @param page page number for pageination.
   * @param limit for pageination per page.
   * @param keyword to match with participant
   * @return the List of {@link UserProfileResponse}
   */
  @Transactional
  public List<UserProfileResponse> getAllUserInfo(int page, int limit, String keyword) {
    Pageable pageable = PageRequest.of(page - 1, limit);
    List<UserProfile> userProfiles = null;

    try {
      retrieveUserProfiles(keyword, pageable);
    } catch (InterruptedException ex) {
      log.info("Failed searching database for keyword {}", keyword);
    }
    if (userProfiles == null || userProfiles.isEmpty()) {
      return Collections.emptyList();
    }
    return profileConverter.toResponse(userProfiles);
  }

  /**
   * Retrieves Users Profiles based on keyword in a pageable manner
   *
   * @param keyword the search key
   * @param pageable the {@link Pageable}
   * @return the List of {@link UserProfile}.
   * @throws InterruptedException on db failure.
   */
  private List<UserProfile> retrieveUserProfiles(String keyword, Pageable pageable)
      throws InterruptedException {
    if (keyword == null || keyword.isEmpty()) {
      final Page<UserProfile> userProfiles = userDetailsRepository.findAll(pageable);
      return userProfiles.stream().collect(Collectors.toList());
    }
    return userProfileSearchRepository.search(keyword, pageable);
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
    } else {
      profileConverter.updateEntity(profileRequest, userProfile);
    }

    userDetailsRepository.save(userProfile);
    List<WorkExperienceRequest> workExperienceRequests = profileRequest.getWorkExperiences();
    saveWorkExperiences(userProfile, workExperienceRequests);
    return userProfile;
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
