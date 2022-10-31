package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.UserInfoService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * User Controller
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profile/user")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserDetailsController {

  private final UserInfoService userInfoService;

  /**
   * Retrieves all user details.
   *
   * @param request the {@link UserProfileRequest}
   * @param id the user id.
   * @return the {@link UserProfileRequest} as response.
   */
  @PostMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<String> saveUserDetails(
      @PathVariable String id, @Valid @RequestBody UserProfileRequest request) {

    request.setKeyCloakId(id);
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

  /**
   * Retrieves all User information based on the input skillName.
   *
   * @param skillName the {@link String}
   * @param page page number for pageination.
   * @param limit for pageination.
   * @return the List of {@link UserProfileResponse}
   */
  @GetMapping("/skill")
  @ResponseBody
  public ResponseEntity<List<UserProfileResponse>> getUserDetailsFromSkill(
      @RequestParam String skillName, @RequestParam int page, @RequestParam int limit) {
    if (skillName == null) {
      return ResponseEntity.ok(Collections.emptyList());
    }
    List<UserProfileResponse> userProfileResponses =
        userInfoService.getUserInfoBySkill(skillName.toLowerCase(), page, limit);
    return ResponseEntity.ok(userProfileResponses);
  }

  /**
   * Retrieves the all the user information.
   *
   * @param page page number for pageination.
   * @param limit for pageination.
   * @param skill to search for users with given skills
   * @param keyword to search for users
   * @return the {@link ResponseEntity} of generic type.
   */
  @GetMapping("/")
  @ResponseBody
  public ResponseEntity<List<UserProfileResponse>> getAllUserDetails(
      @RequestParam int page,
      @RequestParam int limit,
      @RequestParam(defaultValue = "") String skill,
      @RequestParam(defaultValue = "") String keyword) {
    log.info("Fetching User Details");

    final List<UserProfileResponse> users =
        userInfoService.getAllUserInfo(page, limit, skill, keyword);

    return ResponseEntity.ok(users);
  }

  /**
   * Creates a new user role for the user.
   *
   * @param id the IAM id
   * @param role the user role
   * @return success on role creation.
   */
  @PutMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<Map<String, Boolean>> saveUserRole(
      @PathVariable String id, @RequestParam(value = "role") String role) {
    UserProfileRequest request = new UserProfileRequest();
    request.setKeyCloakId(id);
    request.setRole(role);
    userInfoService.updateUserRole(request);
    return ResponseEntity.ok().build();
  }
}
