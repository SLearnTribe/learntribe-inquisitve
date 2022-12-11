package com.smilebat.learntribe.learntribeinquisitve.controllers;

import static com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService.PageableAssessmentRequest;

import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.PutMapping;
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

  private static final String SUBJECT = "subject";

  /**
   * Retrieves all the related and recommended assessments to User Id
   *
   * @param keyCloakId the user keycloak id
   * @param pageNo the page number of pagination
   * @param pageSize the limit for pagination
   * @param filters the status filters from UI i.e. [COMPLETED, PENDING, BLOCKED, FAILED]
   * @param keyword the search keyword.
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/user")
  @ResponseBody
  @ApiOperation(
      value = "Assessment Retrieval",
      notes = "Fetches assessments based on system recommendation or assigned")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved",
            response = AssessmentResponse.class,
            responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad-Request"),
        @ApiResponse(code = 401, message = "Un-authorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Url Not-found"),
        @ApiResponse(code = 422, message = "Invalid-Data"),
      })
  public ResponseEntity<?> retrieveRecommendedAssessments(
      @AuthenticationPrincipal(expression = SUBJECT) String keyCloakId,
      @RequestParam(value = "page") int pageNo,
      @RequestParam(value = "limit") int pageSize,
      @RequestParam(value = "filters", required = false) String[] filters,
      @RequestParam(defaultValue = "", required = false) String keyword) {

    if (pageNo <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Page Number");
    }

    Pageable paging = PageRequest.of(pageNo - 1, pageSize);

    PageableAssessmentRequest pageRequest =
        PageableAssessmentRequest.builder()
            .paging(paging)
            .filters(filters)
            .keyCloakId(keyCloakId)
            .build();

    List<AssessmentResponse> responses =
        assessmentService.retrieveUserAssessments(pageRequest, keyword);
    //    if (responses.isEmpty()) {
    //      return ResponseEntity.status(422).body("Unable to Create Assessments");
    //    }
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
  @ResponseBody
  @ApiOperation(
      value = "Assessment Creation",
      notes = "Creates assessments based on HR Requirements")
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Successfully created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Fbidden"),
        @ApiResponse(code = 404, message = "Url Not found"),
        @ApiResponse(code = 422, message = "Invalid Data"),
      })
  public ResponseEntity<Boolean> createAssessment(
      @AuthenticationPrincipal(expression = SUBJECT) String keyCloakId,
      @RequestBody AssessmentRequest request) {

    request.setAssignedBy(keyCloakId);
    final Boolean response = assessmentService.createAssessment(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Assigns a existing assessment.
   *
   * @param keyCloakId the IAM user id.
   * @param assigneeEmail the assignee email id.
   * @param assessmentId the assessmend id.
   * @return the {@link AssessmentResponse}.
   */
  @PutMapping(value = "/id/{assessmentId}")
  @ResponseBody
  @ApiOperation(
      value = "Assign Existing Assessment",
      notes = "Assigns already created assessments to a list of candidates")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Url Not found"),
        @ApiResponse(code = 422, message = "Invalid Data"),
      })
  public ResponseEntity<Boolean> assignAssessment(
      @AuthenticationPrincipal(expression = SUBJECT) String keyCloakId,
      @RequestParam String assigneeEmail,
      @PathVariable(value = "assessmentId") Long assessmentId) {

    assessmentService.assignAssessment(keyCloakId, assigneeEmail, assessmentId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * Retrieves the assessment based on assessment id
   *
   * @param assessmentId the assessment id
   * @return {@link ResponseEntity} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{assessmentId}")
  @ResponseBody
  @ApiOperation(
      value = "Retrieves assessment details based on assessment id",
      notes = "Fetches assessments based on the ID ")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved",
            response = AssessmentResponse.class),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Url Not found"),
        @ApiResponse(code = 422, message = "Invalid Data"),
      })
  public ResponseEntity<AssessmentResponse> retrieveAssessment(
      @PathVariable(value = "assessmentId") Long assessmentId) {

    AssessmentResponse response = assessmentService.retrieveAssessment(assessmentId);

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves the assessment based on assessment id
   *
   * @param keyCloakId the IAM user id.
   * @return {@link ResponseEntity} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/preceding")
  @ResponseBody
  @ApiOperation(
      value = "Retrieves previously generated assessments",
      notes = "Fetches foregoing assessments for hr")
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Successfully retrieved",
            response = AssessmentResponse.class)
      })
  public ResponseEntity<List<AssessmentResponse>> retrieveHrAssessments(
      @AuthenticationPrincipal(expression = SUBJECT) String keyCloakId) {

    List<AssessmentResponse> responses = assessmentService.getGeneratedAssessments(keyCloakId);

    return ResponseEntity.ok(responses);
  }
}
