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
import javax.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

  /** Job pagination concept builder. */
  @Getter
  @Setter
  @Builder
  public static class PageableJobRequest {
    private String keyCloakId;
    private Pageable paging;
  }

  /**
   * Retrieves all jobs realted to the user id.
   *
   * @param request the {@link PageableJobRequest} Pageable Job request from job service.
   * @return the List of {@link com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse}.
   */
  @Transactional
  public List<OthersBusinessResponse> retrieveJobs(PageableJobRequest request) {
    String keyCloakId = request.getKeyCloakId();
    Verify.verifyNotNull(keyCloakId, "User Id cannot be null");

    Pageable paging = request.getPaging();
    List<UserObReltn> userObReltns = userObReltnRepository.findByUserId(keyCloakId);

    if (userObReltns == null || userObReltns.isEmpty()) {
      return Collections.emptyList();
    }

    final Long[] jobIds =
        userObReltns.stream().map(UserObReltn::getJobId).toArray(s -> new Long[s]);

    List<OthersBusiness> jobs = jobRepository.findAllById(paging, jobIds);

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
