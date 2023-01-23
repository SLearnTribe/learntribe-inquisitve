package com.smilebat.learntribe.learntribeinquisitve.services.strategies.experiences.strategy;

import com.smilebat.learntribe.dataaccess.jpa.entity.SideProject;
import com.smilebat.learntribe.learntribeinquisitve.services.strategies.experiences.DefaultExperienceStrategy;
import com.smilebat.learntribe.learntribeinquisitve.services.strategies.experiences.context.SideProjectContext;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Business Logic implementation for computing Side Project Experiences.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Slf4j
@Service
public class SideProjectStrategy
    extends DefaultExperienceStrategy<SideProjectContext, SideProject> {

  @Override
  public void updateExperiences(SideProjectContext context) {
    log.info("Updating Side Projects for User");
    super.updateExperiences(context);
    context.getProfile().setSideProjects(new TreeSet<>(context.getUpdatedExperiences()));
  }
}
