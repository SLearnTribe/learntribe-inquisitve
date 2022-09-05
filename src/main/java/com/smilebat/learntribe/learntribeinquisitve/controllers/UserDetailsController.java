package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.SkillService;
import com.smilebat.learntribe.learntribeinquisitve.services.UserInfoService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profile/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserDetailsController {

  private final UserInfoService userInfoService;
  private final SkillService skillService;

  /**
   * Retrieves all user details.
   *
   * @param request the {@link UserProfileRequest}
   * @return the {@link UserProfileRequest} as response.
   */
  @PostMapping
  @ResponseBody
  public ResponseEntity<String> saveUserDetails(@Valid @RequestBody UserProfileRequest request) {

    userInfoService.saveUserInfo(request);

    return ResponseEntity.status(HttpStatus.OK).body("Created User");
  }

  /**
   * Retrieves the user from IAM based on user id.
   *
   * @param id the user id
   * @return the Respresentation of User from IAM.
   */
  @GetMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<?> getUserDetails(@PathVariable String id) {
    log.info("Fetching User Details");

    if (id == null || id.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID cannot be null/empty");
    }

    final UserProfileResponse userInfo = userInfoService.getUserInfo(id);
    return ResponseEntity.ok(userInfo);
  }

  @GetMapping("/skill")
  @ResponseBody
  public ResponseEntity<List<UserProfileResponse>> getUserDetailsFromSkill(
      @RequestParam String skillName) {
    List<UserProfileResponse> userProfileResponses = skillService.getUserInfo(skillName);
    return ResponseEntity.ok(userProfileResponses);
  }
}
