package com.smilebat.learntribe.learntribeinquisitve.services;

import static com.smilebat.learntribe.learntribeclients.twilio.EmailService.TwEmail;

import com.google.common.base.Verify;
import com.smilebat.learntribe.dataaccess.OthersBusinessRepository;
import com.smilebat.learntribe.dataaccess.UserProfileRepository;
import com.smilebat.learntribe.dataaccess.jpa.entity.OthersBusiness;
import com.smilebat.learntribe.dataaccess.jpa.entity.UserProfile;
import com.smilebat.learntribe.learntribeclients.twilio.EmailService;
import com.smilebat.learntribe.mail.HrMailRequest;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Mail Service.
 *
 * <p>Copyright &copy; 2023 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
@RequiredArgsConstructor
public class MailService {

  private final EmailService service;
  private final UserProfileRepository profileRepository;

  private final OthersBusinessRepository jobRepository;

  /**
   * Sends a email b/w to and from.
   *
   * @param mailRequest the {@link HrMailRequest}.
   */
  @Transactional
  public void sendEmail(HrMailRequest mailRequest) {
    Verify.verifyNotNull(mailRequest, "Mail Request cannot be null");
    final String keyCloakId = mailRequest.getFromId();
    Verify.verifyNotNull(keyCloakId, "KeycloakId cannot be null");
    final UserProfile hrProfile = profileRepository.findByKeyCloakId(keyCloakId);

    String toEmail = mailRequest.getToEmail();
    final UserProfile candidateProfile = profileRepository.findByEmail(toEmail);

    if (candidateProfile == null) {
      throw new IllegalArgumentException("Invalid candidate email");
    }

    final Optional<OthersBusiness> business = jobRepository.findById(mailRequest.getJobId());

    if (!business.isPresent()) {
      throw new IllegalArgumentException("Invalid Job Id");
    }

    OthersBusiness job = business.get();

    TwEmail twEmail =
        TwEmail.builder()
            .fromEmail("sainandanpai@gmail.com")
            .toEmail("smilebat96@gmail.com")
            .subject("Invitation for Interview with " + job.getBusinessName())
            .body(
                "Dear "
                    + candidateProfile.getName()
                    + ","
                    + "\nWe are pleased to inform you that you are considered for "
                    + job.getTitle()
                    + "\nPlease find the meeting link below: \n"
                    + mailRequest.getMeetingUrl())
            .build();

    service.sendMail(twEmail);
  }
}
