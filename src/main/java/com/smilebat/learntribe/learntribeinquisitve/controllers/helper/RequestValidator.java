package com.smilebat.learntribe.learntribeinquisitve.controllers.helper;

import com.smilebat.learntribe.learntribevalidator.learntribeexceptions.BeanValidationException;
import org.springframework.stereotype.Component;

/** Validator class */
@Component
public class RequestValidator {

  /**
   * Validates the string input
   *
   * @param keyCloakId the iam id
   */
  public void validateUserId(String keyCloakId) {
    if (keyCloakId == null || keyCloakId.isEmpty()) {
      throw new BeanValidationException("keyCloakId", "Invalid User Id");
    }
  }
}
