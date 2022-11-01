package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.response.AnalyticsResponse;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.inquisitve.response.CandidateActivitiesResponse;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.AnalyticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Assessment Controller
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/analytics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  /**
   * Retrieves all the related activities of User.
   *
   * @param keyCloakId the user keycloak id
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/candidate/id/{id}/activities")
  @ResponseBody
  public ResponseEntity<CandidateActivitiesResponse> evaluateCandidateActivities(
      @PathVariable(value = "id") String keyCloakId) {

    CandidateActivitiesResponse response = analyticsService.retrieveCandidateActivities(keyCloakId);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves all the considered jobs of candidate.
   *
   * @param keyCloakId the user keycloak id
   * @return the {@link List} of {@link OthersBusinessResponse}
   */
  @GetMapping(value = "/candidate/id/{id}")
  @ResponseBody
  public ResponseEntity<List<OthersBusinessResponse>> evaluateConsideredJobs(
      @PathVariable(value = "id") String keyCloakId) {

    List<OthersBusinessResponse> response = analyticsService.retrieveConsideredJobs(keyCloakId);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves all the related assessments to User Id
   *
   * @param keyCloakId the user keycloak id
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/hr/id/{id}")
  @ResponseBody
  public ResponseEntity<AnalyticsResponse> evaluateHrAnalytics(
      @PathVariable(value = "id") String keyCloakId) {

    AnalyticsResponse response = analyticsService.retrieveDashBoardStatus(keyCloakId);

    return ResponseEntity.ok(response);
  }
}
