package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.assessment.response.AssessmentStatusResponse;
import com.smilebat.learntribe.dataaccess.AssessmentRepository;
import com.smilebat.learntribe.dataaccess.JobsSearchRepository;
import com.smilebat.learntribe.dataaccess.OthersBusinessRepository;
import com.smilebat.learntribe.dataaccess.UserAstReltnRepository;
import com.smilebat.learntribe.dataaccess.UserObReltnRepository;
import com.smilebat.learntribe.dataaccess.jpa.entity.OthersBusiness;
import com.smilebat.learntribe.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.dataaccess.jpa.entity.UserObReltn;
import com.smilebat.learntribe.enums.AssessmentStatus;
import com.smilebat.learntribe.enums.HiringStatus;
import com.smilebat.learntribe.enums.UserObReltnType;
import com.smilebat.learntribe.inquisitve.JobRequest;
import com.smilebat.learntribe.inquisitve.JobUpdateRequest;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.JobConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.smilebat.learntribe.learntribevalidator.learntribeexceptions.InvalidDataException;
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
 * @author Likith
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

  private final UserObReltnRepository userObReltnRepository;

  private final OthersBusinessRepository jobRepository;

  private final JobConverter jobConverter;

  private final UserAstReltnRepository userAstReltnRepository;

  private final JobsSearchRepository jobSearchRepository;
  private final AssessmentRepository assessmentRepository;
  private final UserProfileService userProfileService;

  /** Job pagination concept builder. */
  @Getter
  @Setter
  @Builder
  public static class PageableJobRequest {
    private String keyCloakId;
    private Pageable paging;
  }

  /**
   * Updates a given job based on job id
   *
   * @param request the {@link JobUpdateRequest}
   * @return the updated {@link OthersBusinessResponse}
   */
  @Transactional
  public OthersBusinessResponse updateJob(JobUpdateRequest request) {
    Verify.verifyNotNull(request, "Job Request cannot be null");
    String keycloakId = request.getCreatedBy();
    Long jobId = request.getId();
    Verify.verifyNotNull(jobId, "Job Id cannot be null");
    Optional<OthersBusiness> existingJobEntity = jobRepository.findById(jobId);
    if (!existingJobEntity.isPresent()) {
      return new OthersBusinessResponse();
    }

    final UserObReltn userObReltn = userObReltnRepository.findByRelatedJobId(keycloakId, jobId);
    OthersBusiness updatedJob = jobConverter.toEntity(request);
    updatedJob.setId(jobId);
    updateUserObReltn(userObReltn, updatedJob);
    userObReltnRepository.save(userObReltn);
    jobRepository.save(updatedJob);
    return jobConverter.toResponse(updatedJob);
  }

  /**
   * Retrieves all jobs realted to the user id.
   *
   * @param request the {@link PageableJobRequest} Pageable Job request from job service.
   * @param keyword the search word.
   * @return the List of {@link com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse}.
   */
  @Transactional
  public List<OthersBusinessResponse> retrieveJobs(PageableJobRequest request, String keyword) {
    String keyCloakId = request.getKeyCloakId();
    Verify.verifyNotNull(keyCloakId, "User Id cannot be null");
    Pageable paging = request.getPaging();

    List<UserObReltn> userObReltns = getUserObReltns(keyCloakId, keyword, paging);
    if (userObReltns == null || userObReltns.isEmpty()) {
      return Collections.emptyList();
    }
    final Long[] jobIds =
        userObReltns.stream().map(UserObReltn::getJobId).toArray(s -> new Long[s]);
    List<OthersBusiness> jobs = jobRepository.findAllById(paging, jobIds);

    final List<Long> existingAssessmentIds = assessmentRepository.findByRelatedJobs(jobIds);
    final List<UserAstReltn> usrAstReltns =
        userAstReltnRepository.findAllByUserAstReltn(keyCloakId, existingAssessmentIds);

    List<OthersBusinessResponse> responses = jobConverter.toResponse(jobs);
    responses.forEach(jobResponse -> updateUserAssessmentStatus(usrAstReltns, jobResponse));
    return responses;
  }

  private List<UserObReltn> getUserObReltns(String keyCloakId, String keyword, Pageable paging) {
    if (keyword == null || keyword.isEmpty()) {
      return userObReltnRepository.findByUserId(keyCloakId, paging);
    }
    try {
      return jobSearchRepository.search(keyword, keyCloakId, paging);
    } catch (InterruptedException ex) {
      throw new IllegalArgumentException("No Jobs related to search key", ex);
    }
  }

  private void updateUserAssessmentStatus(
      List<UserAstReltn> userAstReltns, OthersBusinessResponse jobResponse) {
    String requiredSkills = jobResponse.getRequiredSkills();
    if (requiredSkills != null && !requiredSkills.isEmpty()) {
      String[] sanitizedSkills = requiredSkills.toUpperCase().split(",");
      final List<AssessmentStatusResponse> statusResponses =
          Arrays.stream(sanitizedSkills)
              .map(String::trim)
              .map(getAssessmentStatusResponse(userAstReltns))
              .collect(Collectors.toList());
      jobResponse.setRequiredAssessments(statusResponses);
    }
  }

  private Function<String, AssessmentStatusResponse> getAssessmentStatusResponse(
      List<UserAstReltn> userAstReltns) {
    return skill ->
        userAstReltns
            .stream()
            .filter(userAstReltn -> skill.equals(userAstReltn.getAssessmentTitle()))
            .map(this::createAssessmentStatusResponse)
            .findAny()
            .orElseGet(createDefaultStatusResponse(skill));
  }

  private Supplier<AssessmentStatusResponse> createDefaultStatusResponse(String requiredSkill) {
    AssessmentStatusResponse response = new AssessmentStatusResponse();
    response.setStatus(AssessmentStatus.NOT_ASSIGNED);
    response.setSkill(requiredSkill);
    return () -> response;
  }

  private AssessmentStatusResponse createAssessmentStatusResponse(UserAstReltn userAstReltn) {
    if (userAstReltn == null) {
      return new AssessmentStatusResponse();
    }
    AssessmentStatusResponse statusResponse = new AssessmentStatusResponse();
    statusResponse.setId(userAstReltn.getAssessmentId());
    statusResponse.setSkill(userAstReltn.getAssessmentTitle());
    statusResponse.setStatus(userAstReltn.getStatus());
    return statusResponse;
  }

  /**
   * Creates a job as per the user requirements.
   *
   * @param request the {@link JobRequest}.
   */
  @Transactional
  public void createJob(JobRequest request) {
    String userId = request.getCreatedBy();
    Verify.verifyNotNull(userId, "User Id cannot be null");
    Verify.verifyNotNull(request, "Job Request cannot be null");

    OthersBusiness newJob = jobConverter.toEntity(request);
    jobRepository.save(newJob);

    UserObReltn userObReltn = createUserObReltn(userId, newJob);
    userObReltnRepository.save(userObReltn);
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
    userObReltn.setBusinessName(createdJob.getBusinessName());
    userObReltn.setLocation(createdJob.getLocation());
    userObReltn.setRequiredSkills(createdJob.getRequiredSkills());
    userObReltn.setTitle(createdJob.getTitle());
    return userObReltn;
  }

  /**
   * Updates a User Job relation object.
   *
   * @param userObReltn the keyCloak user Id
   * @param updatedJob the {@link OthersBusiness}
   */
  private void updateUserObReltn(UserObReltn userObReltn, OthersBusiness updatedJob) {
    userObReltn.setJobId(updatedJob.getId());
    userObReltn.setUserObReltn(UserObReltnType.HR_RECRUITER);
    userObReltn.setBusinessName(updatedJob.getBusinessName());
    userObReltn.setLocation(updatedJob.getLocation());
    userObReltn.setRequiredSkills(updatedJob.getRequiredSkills());
    userObReltn.setTitle(updatedJob.getTitle());
  }

  /**
   * Creates a User Job relation object for candidate.
   *
   * @param emailId the keyCloak user Id
   * @param jobId the Job ID
   */
  public void scheduleCall(String emailId, Long jobId) {
      String keycloakId = userProfileService.getUserByEmail(emailId).getKeyCloakId();
      UserObReltn userObReltn = new UserObReltn();
      userObReltn.setJobId(jobId);
      userObReltn.setUserId(keycloakId);
      userObReltn.setHiringStatus(HiringStatus.SHORTLISTED);
      userObReltn.setUserObReltn(UserObReltnType.CANDIDATE);
      userObReltnRepository.save(userObReltn);
  }
}
