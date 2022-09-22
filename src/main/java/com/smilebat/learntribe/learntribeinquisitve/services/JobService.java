package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.OthersBusinessRequest;
import com.smilebat.learntribe.inquisitve.UserObReltnType;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.JobConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.OthersBusinessRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserObReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Job Service to hold the business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

  private final UserObReltnRepository userObReltnRepository;

  private final OthersBusinessRepository jobRepository;

  private final JobConverter jobConverter;

  /**
   * Retrieves all jobs realted to the user id.
   *
   * @param userId the {@link Long} keycloak id.
   * @return the List of {@link com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse}.
   */
  public List<OthersBusinessResponse> retrieveJobs(String userId) {
    Verify.verifyNotNull(userId, "User Id cannot be null");

    List<UserObReltn> userObReltns = null;

    userObReltns = userObReltnRepository.findByUserId(userId);

    if (userObReltns == null || userObReltns.isEmpty()) {
      return Collections.emptyList();
    }

    List<Long> jobIds =
        userObReltns.stream().map(UserObReltn::getJobId).collect(Collectors.toList());

    List<OthersBusiness> jobs = jobRepository.findAllById(jobIds);

    return jobConverter.toResponse(jobs);
  }

  /**
   * Creates a job as per the user requirements.
   *
   * @param userId the keycloak id of the recruiter.
   * @param request the {@link OthersBusinessRequest}.
   * @return the {@link OthersBusinessResponse}.
   */
  @Transactional
  public OthersBusinessResponse createJob(String userId, OthersBusinessRequest request) {
    Verify.verifyNotNull(userId, "User Id cannot be null");
    Verify.verifyNotNull(request, "Job Request cannot be null");

    OthersBusiness newJob = jobConverter.toEntity(request);
    newJob.setCreatedBy(userId);
    jobRepository.save(newJob);

    UserObReltn userObReltn = createUserObReltn(userId, newJob);
    userObReltnRepository.save(userObReltn);

    return jobConverter.toResponse(newJob);
  }

  /**
   * Creates a User Job relation object.
   *
   * @param userId the keyCloak user Id
   * @param createdJob the {@link OthersBusiness}
   * @return the {@link UserObReltn}
   */
  private UserObReltn createUserObReltn(String userId, OthersBusiness createdJob) {
    UserObReltn userObReltn = new UserObReltn();
    userObReltn.setUserId(userId);
    userObReltn.setJobId(createdJob.getId());
    userObReltn.setUserObReltn(UserObReltnType.HR_RECRUITER);
    return userObReltn;
  }
}
