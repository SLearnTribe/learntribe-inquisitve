package com.smilebat.learntribe.learntribeinquisitve.services.strategies.context;

import com.smilebat.learntribe.dataaccess.SideProjectRepository;
import com.smilebat.learntribe.dataaccess.jpa.entity.SideProject;
import com.smilebat.learntribe.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.inquisitve.SideProjectRequest;
import com.smilebat.learntribe.learntribeinquisitve.converters.SideProjectsConverter;
import com.smilebat.learntribe.learntribeinquisitve.services.strategies.ExperienceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Context for education experience
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
@RequiredArgsConstructor
public final class SideProjectContext
    extends ExperienceContext<SideProject, SideProjectRepository> {

  private final SideProjectsConverter converter;
  private final SideProjectRepository repository;

  @Getter @Setter private Collection<SideProjectRequest> request;

  @Setter @Getter private UserProfile profile;

  @Override
  public SideProjectRepository getRepository() {
    return this.repository;
  }

  @Override
  public Set<SideProject> getRequestExperiences() {
    return request == null ? Collections.emptySet() : converter.toEntities(request);
  }

  @Override
  public Set<SideProject> getExistingExperiences() {
    return profile.getSideProjects();
  }
}
