package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitive.request.UserProfileDTO;
import com.smilebat.learntribe.learntribeinquisitve.services.UserInfoServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * @author Pai,Sai Nandan
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/userinfo")
@RequiredArgsConstructor
public class UserDetailsController {

  @Autowired UserInfoServices userInfoServices;

  /**
   * Retrieves all users from IAM. Get the Data from Controler
   *
   * @return an object of {@link UserProfileDTO}
   */
  @PostMapping
  @ResponseBody
  public ResponseEntity<UserProfileDTO> saveUserDetails(@RequestBody UserProfileDTO profileReq) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    UserProfileDTO respObj = userInfoServices.SaveUserInfo(profileReq);

    return ResponseEntity.ok(respObj);
  }
}
