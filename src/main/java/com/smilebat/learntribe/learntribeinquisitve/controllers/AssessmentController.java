package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/api/v1/assessments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AssessmentController {

  private final AssessmentService assessmentService;

  /**
   * Retrieves all the related assessments to User Id
   *
   * @param keyCloakId the user keycloak id
   * @param pageSize page size for pagination
   * @param pageNo page number for pagination
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/user/id/{id}/{no}/{size}")
  @ResponseBody
  public ResponseEntity<List<AssessmentResponse>> retrieveAssessments(
      @PathVariable(value = "id") String keyCloakId,
      @PathVariable(value = "no") int pageNo,
      @PathVariable(value = "size") int pageSize) {

    List<AssessmentResponse> responses = null;

    responses = assessmentService.retrieveAssessments(keyCloakId, pageNo, pageSize);

    return ResponseEntity.ok(responses);
  }

  /**
   * Creates a new assessment.
   *
   * @param keyCloakId the IAM user id.
   * @param pageSize page size for pagination
   * @param pageNo page number for pagination
   * @param request the {@link AssessmentRequest}.
   * @return the {@link AssessmentResponse}.
   */
  @PostMapping(value = "/user/id/{id}/{no}/{size}")
  public ResponseEntity<AssessmentResponse> createAssessment(
      @PathVariable(value = "id") String keyCloakId,
      @RequestBody AssessmentRequest request,
      @PathVariable(value = "no") int pageNo,
      @PathVariable(value = "size") int pageSize) {

    final AssessmentResponse response =
        assessmentService.createAssessment(keyCloakId, request, pageNo, pageSize);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves all the related assessments to User Id
   *
   * @param assessmentId the user keycloak id
   * @return {@link ResponseEntity} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<AssessmentResponse> retrieveAssessment(
      @PathVariable(value = "id") Long assessmentId) {

    AssessmentResponse response = assessmentService.retrieveAssessment(assessmentId);

    return ResponseEntity.ok(response);
  }
}
