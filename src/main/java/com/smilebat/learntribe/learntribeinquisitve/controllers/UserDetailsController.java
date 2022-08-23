package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

  private final UserInfoService service;

  /**
   * Retrieves all user details.
   *
   * @param request the {@link UserProfileRequest}
   * @return the {@link UserProfileRequest} as response.
   */
  @PostMapping
  @ResponseBody
  public ResponseEntity<String> saveUserDetails(@RequestBody UserProfileRequest request) {

    Long userId = service.saveUserInfo(request);

    return ResponseEntity.ok(userId.toString());
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

    final UserProfileResponse userInfo = service.getUserInfo(id);
    return ResponseEntity.ok(userInfo);
  }
}
