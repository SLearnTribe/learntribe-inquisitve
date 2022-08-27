package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.WorkExperienceRequest;
import com.smilebat.learntribe.inquisitve.response.WorkExperienceResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.WorkExperience;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Work Experience Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public final class WorkExperienceConverter {

  /**
   * Converts {@link WorkExperienceRequest} to {@link WorkExperience}.
   *
   * @param request the {@link WorkExperienceRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the {@link WorkExperience}
   */
  public WorkExperience toEntity(WorkExperienceRequest request, UserProfile profile) {
    WorkExperience workExperience = new WorkExperience();
    workExperience.setId(request.getId());
    workExperience.setDesignation(request.getDesignation());
    workExperience.setOrgName(request.getOrgName());
    workExperience.setStartDate(request.getStartDate());
    workExperience.setEndDate(request.getEndDate());
    workExperience.setYears(request.getYears());
    workExperience.setLocation(request.getLocation());
    workExperience.setUserDetails(profile);
    return workExperience;
  }

  /**
   * Converts List of {@link WorkExperienceRequest} to List of {@link WorkExperience}.
   *
   * @param requestList the list of {@link WorkExperienceRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the {@link WorkExperience}
   */
  public Set<WorkExperience> toEntities(
      Collection<WorkExperienceRequest> requestList, UserProfile profile) {
    return requestList
        .stream()
        .map(request -> this.toEntity(request, profile))
        .collect(Collectors.toSet());
  }

  /**
   * Converts {@link WorkExperience} to {@link WorkExperienceResponse}.
   *
   * @param workExperience the {@link WorkExperience}
   * @return the {@link WorkExperienceResponse}
   */
  public WorkExperienceResponse toResponse(WorkExperience workExperience) {
    WorkExperienceResponse response = new WorkExperienceResponse();
    response.setId(workExperience.getId());
    response.setDesignation(workExperience.getDesignation());
    response.setLocation(workExperience.getLocation());
    response.setEndDate(workExperience.getEndDate());
    response.setStartDate(workExperience.getStartDate());
    response.setYears(workExperience.getYears());
    response.setOrgName(workExperience.getOrgName());
    return response;
  }

  /**
   * Converts List of {@link WorkExperience} to List of {@link WorkExperienceResponse}.
   *
   * @param workExperiences the list of {@link WorkExperience}
   * @return the list of {@link WorkExperienceResponse}
   */
  public List<WorkExperienceResponse> toResponse(List<WorkExperience> workExperiences) {
    return workExperiences.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
