package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.response.SkillResponse;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.inquisitve.response.WorkExperienceResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.WorkExperience;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Profile Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
@RequiredArgsConstructor
public class UserProfileConverter {
  private final WorkExperienceConverter workExperienceConverter;
  private final SkillConverter skillConverter;

  /**
   * Updates the entity
   *
   * @param request the {@link UserProfileRequest}
   * @param userProfile the {@link UserProfile}
   */
  public void updateEntity(UserProfileRequest request, UserProfile userProfile) {
    userProfile.setKeyCloakId(request.getKeyCloakId());
    userProfile.setName(request.getName());
    userProfile.setEmail(request.getEmail());
    userProfile.setCountry(request.getCountry());
    userProfile.setLinkedIn(request.getLinkedIn());
    userProfile.setGitHub(request.getGitHub());
    userProfile.setAbout(request.getAbout());
    userProfile.setPhone(request.getPhone());
  }

  /**
   * Converts {@link UserProfileRequest} to {@link UserProfile}.
   *
   * @param request the {@link UserProfileRequest}
   * @return the {@link UserProfile}
   */
  public UserProfile toEntity(UserProfileRequest request) {
    UserProfile profile = new UserProfile();
    profile.setKeyCloakId(request.getKeyCloakId());
    profile.setName(request.getName());
    profile.setEmail(request.getEmail());
    profile.setCountry(request.getCountry());
    profile.setLinkedIn(request.getLinkedIn());
    profile.setGitHub(request.getGitHub());
    profile.setAbout(request.getAbout());
    profile.setPhone(request.getPhone());
    return profile;
  }

  /**
   * Converts List of {@link UserProfileRequest} to List of {@link UserProfile}.
   *
   * @param requests the {@link UserProfileRequest}
   * @return the {@link UserProfile}
   */
  public List<UserProfile> toEntities(final Collection<UserProfileRequest> requests) {
    return requests.stream().map(this::toEntity).collect(Collectors.toList());
  }

  /**
   * Converts the {@link UserProfile} to {@link UserProfileResponse}.
   *
   * @param profile the {@link UserProfile}
   * @return the {@link UserProfileResponse}
   */
  public UserProfileResponse toResponse(UserProfile profile) {
    UserProfileResponse response = new UserProfileResponse();
    response.setKeyCloakId(profile.getKeyCloakId());
    response.setAbout(profile.getAbout());
    response.setCountry(profile.getCountry());
    response.setEmail(profile.getEmail());
    response.setGitHub(profile.getGitHub());
    response.setLinkedIn(profile.getLinkedIn());
    response.setUserProfileId(profile.getId());
    response.setName(profile.getName());
    response.setPhone(profile.getPhone());

    final Set<WorkExperience> experienceSet = profile.getWorkExperiences();
    List<WorkExperienceResponse> workExperienceResponses = Collections.emptyList();
    if (experienceSet != null && !experienceSet.isEmpty()) {
      workExperienceResponses =
          workExperienceConverter.toResponse(experienceSet.stream().collect(Collectors.toList()));
    }

    final Set<Skill> skillSet = profile.getSkills();
    List<SkillResponse> skillResponses = Collections.emptyList();
    if (skillSet != null && !skillSet.isEmpty()) {
      skillResponses =
          skillConverter.toResponse(profile.getSkills().stream().collect(Collectors.toList()));
    }

    response.setWorkExperiences(workExperienceResponses);
    response.setSkills(skillResponses);
    return response;
  }
}
