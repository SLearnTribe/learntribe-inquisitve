package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.dataaccess.jpa.entity.EducationExperience;
import com.smilebat.learntribe.dataaccess.jpa.entity.SideProject;
import com.smilebat.learntribe.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.dataaccess.jpa.entity.WorkExperience;
import com.smilebat.learntribe.enums.Gender;
import com.smilebat.learntribe.enums.InterviewStatus;
import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.response.CoreUserProfileResponse;
import com.smilebat.learntribe.inquisitve.response.EducationalExpResponse;
import com.smilebat.learntribe.inquisitve.response.SideProjectResponse;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.inquisitve.response.WorkExperienceResponse;
import java.util.Collection;
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
@SuppressWarnings({"PMD.NcssMethodCount", "PMD.NcssCount"})
public class UserProfileConverter {
  private final WorkExperienceConverter workExperienceConverter;

  private final EducationExperienceConverter edExperienceConverter;

  private final SideProjectsConverter sideProjectsConverter;

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
    // userProfile.setGitHub(request.getGitHub());
    userProfile.setAbout(request.getAbout());
    userProfile.setPhone(request.getPhone());
    userProfile.setCurrentCtc(request.getCurrentCtc());
    userProfile.setExpectedCtc(request.getExpectedCtc());
    userProfile.setNoticePeriod(request.getNoticePeriod());
    if (request.getAvailableForInterview() != null) {
      userProfile.setAvailableForInterview(request.getAvailableForInterview());
    }
    String skills = request.getSkills();
    if (skills != null && !skills.isEmpty()) {
      userProfile.setSkills(skills.toUpperCase());
    }
    if (request.getGender() != null) {
      userProfile.setGender(request.getGender());
    }
  }

  /**
   * Converts {@link UserProfileRequest} to {@link UserProfile}.
   *
   * @param request the {@link UserProfileRequest}
   * @return the {@link UserProfile}
   */
  public UserProfile toEntity(UserProfileRequest request) {
    UserProfile profile = new UserProfile();
    updateEntity(request, profile);
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
  public CoreUserProfileResponse toResponse(UserProfile profile) {
    CoreUserProfileResponse response = new CoreUserProfileResponse();
    response.setAbout(profile.getAbout());
    response.setCountry(profile.getCountry());
    response.setEmail(profile.getEmail());
    response.setGitHub(profile.getGitHub());
    response.setLinkedIn(profile.getLinkedIn());
    response.setName(profile.getName());
    response.setPhone(profile.getPhone());
    response.setSkills(profile.getSkills());
    response.setCurrentRole(profile.getCurrentDesignation());
    InterviewStatus status = profile.getAvailableForInterview();
    if (status != null) {
      response.setAvailableForInterview(profile.getAvailableForInterview().name());
    }
    Gender gender = profile.getGender();
    if (gender != null) {
      response.setGender(gender.name());
    }
    response.setExpectedCtc(profile.getExpectedCtc());
    response.setCurrentCtc(profile.getCurrentCtc());
    response.setNoticePeriod(profile.getNoticePeriod());

    Set<WorkExperience> experienceSet = profile.getWorkExperiences();
    if (experienceSet != null && !experienceSet.isEmpty()) {
      List<WorkExperienceResponse> workExperienceResponses =
          workExperienceConverter.toResponse(experienceSet.stream().collect(Collectors.toList()));
      response.setWorkExperiences(workExperienceResponses);
    }

    Set<EducationExperience> edExperienceSet = profile.getEducationExperiences();
    if (edExperienceSet != null && !edExperienceSet.isEmpty()) {
      List<EducationalExpResponse> educationalExpResponses =
          edExperienceConverter.toResponse(edExperienceSet.stream().collect(Collectors.toList()));
      response.setEducationExperiences(educationalExpResponses);
    }

    Set<SideProject> sideProjects = profile.getSideProjects();
    if (sideProjects != null && !sideProjects.isEmpty()) {
      List<SideProjectResponse> sideProjectResponses =
          sideProjectsConverter.toResponse(sideProjects.stream().collect(Collectors.toList()));
      response.setSideProjects(sideProjectResponses);
    }

    return response;
  }

  /**
   * Converts List of {@link UserProfile} to List of {@link UserProfileResponse}.
   *
   * @param profiles the List of {@link UserProfile}
   * @return the List of {@link UserProfileResponse}
   */
  public List<? extends UserProfileResponse> toResponse(Collection<UserProfile> profiles) {
    return profiles.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
