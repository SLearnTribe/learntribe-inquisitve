package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.OthersBusinessRequest;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Job Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public class JobConverter {

  /**
   * Converts the {@link OthersBusiness} to {@link OthersBusinessResponse}.
   *
   * @param jobEntity {@link OthersBusiness}
   * @return the {@link OthersBusinessResponse}
   */
  public OthersBusinessResponse toResponse(OthersBusiness jobEntity) {
    OthersBusinessResponse response = new OthersBusinessResponse();
    response.setId(jobEntity.getId());
    response.setTitle(jobEntity.getTitle());
    response.setDescription(jobEntity.getDescription());
    response.setCreatedBy(jobEntity.getCreatedBy());

    final List<String> requiredSkills = jobEntity.getRequiredSkills();
    if (requiredSkills != null && !requiredSkills.isEmpty()) {
      response.setRequiredSkills(requiredSkills);
    }

    final List<String> rolesAndResponsibilities = jobEntity.getRolesAndResponsibilities();
    if (rolesAndResponsibilities != null && !rolesAndResponsibilities.isEmpty()) {
      response.setRolesAndResponsibilities(rolesAndResponsibilities);
    }

    return response;
  }

  /**
   * Converts a List of {@link OthersBusiness} to List of {@link OthersBusinessResponse}.
   *
   * @param jobEntities the List of {@link OthersBusiness}.
   * @return the List of {@link OthersBusinessResponse}.
   */
  public List<OthersBusinessResponse> toResponse(Collection<OthersBusiness> jobEntities) {
    return jobEntities.stream().map(this::toResponse).collect(Collectors.toList());
  }

  /**
   * Converts the {@link OthersBusinessRequest} to {@link OthersBusiness}
   *
   * @param request the {@link OthersBusinessRequest}
   * @return the {@link OthersBusiness}
   */
  public OthersBusiness toEntity(OthersBusinessRequest request) {
    OthersBusiness jobEntity = new OthersBusiness();

    // jobEntity.setCreatedBy(request.getCreatedBy());
    jobEntity.setDescription(request.getDescription());
    jobEntity.setRequiredSkills(request.getRequiredSkills());
    jobEntity.setRolesAndResponsibilities(request.getRolesAndResponsibilities());
    jobEntity.setTitle(request.getTitle());
    return jobEntity;
  }
}
