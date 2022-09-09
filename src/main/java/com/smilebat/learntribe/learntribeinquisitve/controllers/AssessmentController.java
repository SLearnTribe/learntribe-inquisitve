package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService;
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
@RequestMapping("/api/v1/assessments/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AssessmentController {

  private final AssessmentService service;

  /**
   * Retrieves all the related assessments to User
   *
   * @param id the user id
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<List<AssessmentResponse>> retrieveAssessments(@PathVariable String id) {

    return ResponseEntity.ok(service.retrieveAssessments(id));
  }

  //  public ResponseEntity<AssessmentResponse> createAssessment(@RequestBody Asse)
}