package com.smilebat.learntribe.learntribeinquisitve.controllers;

import static com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService.PageableAssessmentRequest;

import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
   * @param pageNo the page number of pagination
   * @param pageSize the limit for pagination
   * @param filter the status filters from UI
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/user")
  @ResponseBody
  public ResponseEntity<?> retrieveAssessments(
      @AuthenticationPrincipal(expression = "subject") String keyCloakId,
      @RequestParam(value = "page") int pageNo,
      @RequestParam(value = "limit") int pageSize,
      @RequestParam(value = "filter") String[] filter) {

    if (pageNo <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Page Number");
    }

    Pageable paging = PageRequest.of(pageNo - 1, pageSize);

    PageableAssessmentRequest pageRequest =
        PageableAssessmentRequest.builder()
            .paging(paging)
            .filters(filter)
            .keyCloakId(keyCloakId)
            .build();

    List<AssessmentResponse> responses = assessmentService.retrieveUserAssessments(pageRequest);
    if (responses.isEmpty()) {
      return ResponseEntity.status(422).body("Unable to Create Assessments");
    }
    return ResponseEntity.ok(responses);
  }

  /**
   * Creates a new assessment.
   *
   * @param keyCloakId the IAM user id.
   * @param request the {@link AssessmentRequest}.
   * @return the {@link AssessmentResponse}.
   */
  @PostMapping(value = "/user")
  public ResponseEntity<Boolean> createAssessment(
      @AuthenticationPrincipal(expression = "subject") String keyCloakId,
      @RequestBody AssessmentRequest request) {

    final Boolean response = assessmentService.createAssessment(keyCloakId, request);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves the assessment based on assessment id
   *
   * @param assessmentId the assessment id
   * @return {@link ResponseEntity} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{assessmentId}")
  @ResponseBody
  public ResponseEntity<AssessmentResponse> retrieveAssessment(
      @PathVariable(value = "assessmentId") Long assessmentId) {

    AssessmentResponse response = assessmentService.retrieveAssessment(assessmentId);

    return ResponseEntity.ok(response);
  }
}
