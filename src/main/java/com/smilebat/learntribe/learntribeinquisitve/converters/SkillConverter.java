package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.SkillRequest;
import com.smilebat.learntribe.inquisitve.response.SkillResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Skill Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public final class SkillConverter {

  /**
   * Converts the {@link SkillRequest} to {@link Skill}.
   *
   * @param request the {@link SkillRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the {@link Skill}
   */
  public Skill toEntity(SkillRequest request, UserProfile profile) {
    Skill skill = new Skill();
    skill.setId(request.getId());
    skill.setSkillName(request.getSkillName());
    if (profile != null) {
      skill.setUserDetails(profile);
    }
    return skill;
  }

  /**
   * Converts the List of {@link SkillRequest} to List of {@link Skill}.
   *
   * @param requestList the List of {@link SkillRequest}
   * @param profile the mapped {@link UserProfile}
   * @return the List of {@link Skill}
   */
  public Set<Skill> toEntities(Collection<SkillRequest> requestList, UserProfile profile) {
    return requestList
        .stream()
        .map(request -> this.toEntity(request, profile))
        .collect(Collectors.toSet());
  }

  /**
   * Converts the {@link Skill} to {@link SkillResponse}.
   *
   * @param skill the {@link Skill}
   * @return the {@link SkillResponse}
   */
  public SkillResponse toResponse(Skill skill) {
    SkillResponse response = new SkillResponse();
    response.setId(skill.getId());
    response.setSkillName(skill.getSkillName());
    return response;
  }

  /**
   * Converts the List of {@link Skill} to List of {@link SkillResponse}.
   *
   * @param skills the {@link Skill}
   * @return the List of {@link SkillResponse}
   */
  public List<SkillResponse> toResponse(List<Skill> skills) {
    return skills.stream().map(this::toResponse).collect(Collectors.toList());
  }

  /**
   * Converts the {@link Skill} to {@link SkillRequest}.
   *
   * @param skill the {@link Skill}
   * @return the {@link SkillRequest}
   */
  public SkillRequest toRequest(Skill skill) {
    SkillRequest skillRequest = new SkillRequest();
    skillRequest.setId(skill.getId());
    skillRequest.setSkillName(skill.getSkillName());
    return skillRequest;
  }
}
