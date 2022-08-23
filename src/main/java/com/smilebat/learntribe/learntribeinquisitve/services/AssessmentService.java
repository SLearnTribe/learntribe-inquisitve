package com.smilebat.learntribe.learntribeinquisitve.services;

import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.AssessmentConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.AssessmentRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Assessment Service to hold the business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentService {

  private final AssessmentRepository repository;
  private final UserDetailsRepository userRepository;
  private final AssessmentConverter converter;

  /**
   * Retrieves user & skill related assessments.
   *
   * @param keyCloakId the ID provided by IAM (keycloak)
   * @return the List of {@link AssessmentResponse}
   */
  @Transactional
  @Nullable
  public List<AssessmentResponse> retrieveAssessments(String keyCloakId) {
    if (keyCloakId == null) {
      log.info("Keycloak User Id does not exist");
      return Collections.emptyList();
    }

    log.info("Fetching Assessments for User {}", keyCloakId);
    final UserProfile userProfile = userRepository.findByKeyCloakId(keyCloakId);

    if (userProfile == null) {
      log.info("User {} not found", keyCloakId);
      return Collections.emptyList();
    }
    List<Assessment> assessments = repository.findByUserId(userProfile.getId());
    if (assessments == null || assessments.isEmpty()) {
      log.info("No Existing Assessments found for the current user");
      assessments = Assessment.generateMockAssessments();
    }
    return converter.toResponse(assessments);
  }
}
