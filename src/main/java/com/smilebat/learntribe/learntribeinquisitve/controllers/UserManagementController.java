package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.keycloak.response.UserRepresentation;
import com.smilebat.learntribe.learntribeclients.keycloak.KeycloakService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserManagementController {

  private final KeycloakService keycloak;

  /**
   * Retrieves all users from IAM.
   *
   * @return the List of {@link UserRepresentation}
   */
  @GetMapping
  @ResponseBody
  public ResponseEntity<List<UserRepresentation>> fetchAllUsers() {
    log.info("Fetching User Details");

    List<UserRepresentation> users = keycloak.fetchAllUsers();
    if (users == null || users.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyList());
    }
    return ResponseEntity.ok(users);
  }

  /**
   * Retrieves the user from IAM based on user email.
   *
   * @param email the user email.
   * @return the List of {@link UserRepresentation} based on matching email.
   */
  @GetMapping(value = "/email")
  @ResponseBody
  public ResponseEntity<List<UserRepresentation>> fetchUserByEmail(@RequestParam String email) {
    log.info("Fetching User Details");

    //   if (!validator.isValid(email)){
    //     log.error("Invalid Email");
    //     return ResponseEntity.ok(Collections.emptyList());
    //   }

    List<UserRepresentation> users = keycloak.fetchUserByEmail(email);
    if (users == null || users.isEmpty()) {
      return ResponseEntity.ok(Collections.emptyList());
    }
    return ResponseEntity.ok(users);
  }

  /**
   * Retrieves the user from IAM based on user id.
   *
   * @param id the user id
   * @return the Respresentation of User from IAM.
   */
  @GetMapping(value = "/id/{id}")
  @ResponseBody
  public ResponseEntity<UserRepresentation> fetchUserById(@PathVariable String id) {
    log.info("Fetching User Details");

    UserRepresentation users = keycloak.fetchUserById(id);
    if (users == null) {
      return ResponseEntity.ok(new UserRepresentation());
    }
    return ResponseEntity.ok(users);
  }
}
