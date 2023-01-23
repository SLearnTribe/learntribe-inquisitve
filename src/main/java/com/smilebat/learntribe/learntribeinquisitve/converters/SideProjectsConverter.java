package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.dataaccess.jpa.entity.SideProject;
import com.smilebat.learntribe.inquisitve.SideProjectRequest;
import com.smilebat.learntribe.inquisitve.response.SideProjectResponse;
import com.smilebat.learntribe.learntribeinquisitve.util.Commons;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Converter for the User Side Projects.
 *
 * <p>Copyright &copy; 2022 Smile .Bat.
 *
 * @author Pai,Sai Nandan.
 */
@Component
@RequiredArgsConstructor
public class SideProjectsConverter {
  private final Commons commons;

  /**
   * Converts {@link SideProjectRequest} to {@link SideProject}.
   *
   * @param request the {@link SideProjectRequest}
   * @return the {@link SideProject}
   */
  public SideProject toEntity(SideProjectRequest request) {
    SideProject sideProject = new SideProject();
    sideProject.setId(request.getId());
    sideProject.setName(request.getName());
    sideProject.setDescription(request.getDescription());
    sideProject.setUrl(request.getUrl());
    sideProject.setSkills(request.getSkills());
    String startDate = request.getStartDate();
    String endDate = request.getEndDate();
    if (startDate != null) {
      sideProject.setStartDate(commons.toInstant(startDate));
    }
    if (endDate != null) {
      sideProject.setEndDate(commons.toInstant(endDate));
    }
    return sideProject;
  }

  /**
   * Converts List of {@link SideProjectRequest} to Set of {@link SideProject}.
   *
   * @param experiences the list of {@link SideProjectRequest}.
   * @return the set of {@link SideProject}.
   */
  public Set<SideProject> toEntities(Collection<SideProjectRequest> experiences) {
    return experiences.stream().map(this::toEntity).collect(Collectors.toSet());
  }

  /**
   * Converts {@link SideProject} to {@link SideProjectResponse}.
   *
   * @param sideProject the {@link SideProject}
   * @return the {@link SideProjectResponse}
   */
  public SideProjectResponse toResponse(SideProject sideProject) {
    SideProjectResponse response = new SideProjectResponse();
    response.setId(sideProject.getId());
    response.setName(sideProject.getName());
    response.setDescription(sideProject.getDescription());
    response.setUrl(sideProject.getUrl());
    response.setSkills(sideProject.getSkills());
    Instant startDate = sideProject.getStartDate();
    Instant endDate = sideProject.getEndDate();
    if (startDate != null) {
      response.setStartDate(commons.formatInstant.apply(startDate));
    }
    if (endDate != null) {
      response.setEndDate(commons.formatInstant.apply(endDate));
    }
    return response;
  }

  /**
   * Converts List of {@link SideProject} to List of {@link SideProjectResponse}.
   *
   * @param experiences the list of {@link SideProject}
   * @return the list of {@link SideProjectResponse}
   */
  public List<SideProjectResponse> toResponse(List<SideProject> experiences) {
    return experiences.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
