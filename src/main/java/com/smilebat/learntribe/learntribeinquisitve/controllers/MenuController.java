package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.UserRole;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeinquisitve.services.AssessmentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
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
@RequestMapping("/api/v1/menu")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MenuController {

  private final AssessmentService assessmentService;
  private final UserDetailsRepository userDetailsRepository;

  /**
   * Retrieves all the related assessments to User Id
   *
   * @param keyCloakId the user keycloak id
   * @return the {@link List} of {@link AssessmentResponse}
   */
  @GetMapping(value = "/id/{id}")
  @ResponseBody
  @Transactional
  public ResponseEntity<Map<?, ?>> retrieveMenu(@PathVariable(value = "id") String keyCloakId) {

    List<AssessmentResponse> responses = null;

    UserProfile profile = userDetailsRepository.findByKeyCloakId(keyCloakId);
    UserRole userRole = UserRole.ROLE_CANDIDATE;
    Map<Integer, String> map = getCandidateMenu();

    if (profile.getRole() != null) {
      userRole = UserRole.valueOf(profile.getRole().name());
    }

    if (userRole.name().equals(UserRole.ROLE_HR.name())) {
      map = getHrMenu();
    }

    return ResponseEntity.ok(map);
  }

  private Map<Integer, String> getHrMenu() {
    Map<Integer, String> map = new HashMap<>();
    map.put(1, "Dashboard");
    map.put(2, "Applicants");
    map.put(3, "Assessments");
    map.put(4, "Jobs");
    return map;
  }

  private Map<Integer, String> getCandidateMenu() {
    Map<Integer, String> map = new HashMap<>();
    map.put(1, "Dashboard");
    map.put(2, "Assessments");
    map.put(3, "Jobs");
    map.put(4, "Help");
    return map;
  }
}
