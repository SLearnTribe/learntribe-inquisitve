package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.OthersBusinessRequest;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.JobService;
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
@RequestMapping("/api/v1/jobs/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class JobController {

  private final JobService jobService;

  /**
   * Fetchs all the Jobs related to the User id.
   *
   * @param keyCloakId the acual IAM user Id
   * @return the List of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{id}")
  public ResponseEntity<List<OthersBusinessResponse>> retrieveJob(
      @PathVariable(value = "id") String keyCloakId) {

    // return ResponseEntity.ok(service.retrieveAssessments(id));
    List<OthersBusinessResponse> responses = null;
    responses = jobService.retrieveJobs(keyCloakId);

    return ResponseEntity.ok(responses);
  }

  /**
   * Creates a new Job Post.
   *
   * @param keyCloakId the user IAM id
   * @param request the {@link OthersBusinessRequest}
   * @return the {@link OthersBusinessResponse}
   */
  @PostMapping(value = "/id/{id}")
  public ResponseEntity<OthersBusinessResponse> createJob(
      @PathVariable String keyCloakId, @RequestBody OthersBusinessRequest request) {

    final OthersBusinessResponse response = jobService.createJob(keyCloakId, request);

    return ResponseEntity.ok(response);
  }
}
