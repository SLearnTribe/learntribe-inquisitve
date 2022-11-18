package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.EducationalExpRequest;
import com.smilebat.learntribe.inquisitve.WorkExperienceRequest;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.EducationExperience;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.WorkExperience;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Education Experience Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public class EducationExperienceConverter {

  /**
   * Converts {@link EducationalExpRequest} to {@link EducationExperience}.
   *
   * @param request the {@link WorkExperienceRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the {@link WorkExperience}
   */
  public EducationExperience toEntity(EducationalExpRequest request, UserProfile profile) {
    EducationExperience edExperience = new EducationExperience();
    edExperience.setId(request.getId());
    edExperience.setDegree(request.getDegree());
    edExperience.setCollegeName(request.getCollegeName());
    edExperience.setFieldOfStudy(request.getFieldOfStudy());
    edExperience.setDateOfCompletion(request.getDateOfCompletion());
    edExperience.setUserProfile(profile);
    return edExperience;
  }

  /**
   * Converts List of {@link EducationalExpRequest} to List of {@link EducationExperience}.
   *
   * @param requestList the list of {@link WorkExperienceRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the {@link WorkExperience}
   */
  public Set<EducationExperience> toEntities(
      Collection<EducationalExpRequest> requestList, UserProfile profile) {
    return requestList
        .stream()
        .map(request -> this.toEntity(request, profile))
        .collect(Collectors.toSet());
  }
}
