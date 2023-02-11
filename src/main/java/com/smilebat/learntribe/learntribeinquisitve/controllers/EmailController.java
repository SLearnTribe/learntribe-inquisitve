package com.smilebat.learntribe.learntribeinquisitve.controllers;

import com.smilebat.learntribe.inquisitve.UserProfileRequest;
import com.smilebat.learntribe.learntribeinquisitve.services.MailService;
import com.smilebat.learntribe.mail.HrMailRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Email Controller
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 * @author Likith
 */
@Slf4j
@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmailController {

  private final MailService emailService;

  /**
   * Send email from hr.
   *
   * @param request the {@link UserProfileRequest}
   * @param id the user id.
   * @return the {@link UserProfileRequest} as response.
   */
  @PostMapping
  @ResponseBody
  @ApiOperation(value = "Send email to candidates", notes = "Email post")
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Email Sent successfully"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Url Not found"),
      })
  @ApiImplicitParam(
          name = "Authorization",
          value = "Access Token",
          required = true,
          allowEmptyValue = false,
          paramType = "header",
          dataTypeClass = String.class,
          example = "Bearer access_token")  public ResponseEntity<String> saveUserDetails(
      @AuthenticationPrincipal(expression = "subject") String id,
      @Valid @RequestBody HrMailRequest request) {
    request.setFromId(id);
    emailService.sendEmail(request);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
