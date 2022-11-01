package com.smilebat.learntribe.learntribeinquisitve.services;

import static com.smilebat.learntribe.inquisitve.AssessmentStatus.BLOCKED;
import static com.smilebat.learntribe.inquisitve.AssessmentStatus.COMPLETED;
import static com.smilebat.learntribe.inquisitve.AssessmentStatus.PASSED;
import static com.smilebat.learntribe.inquisitve.AssessmentStatus.PENDING;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.AssessmentStatus;
import com.smilebat.learntribe.inquisitve.HiringStatus;
import com.smilebat.learntribe.inquisitve.UserRole;
import com.smilebat.learntribe.inquisitve.response.AnalyticsResponse;
import com.smilebat.learntribe.inquisitve.response.CandidateActivitiesResponse;
import com.smilebat.learntribe.inquisitve.response.CandidateDashboardStatus;
import com.smilebat.learntribe.inquisitve.response.HrDashboardStatus;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeinquisitve.converters.JobConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.OthersBusinessRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserAstReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserObReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Analytics Service to hold the business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

  private final UserAstReltnRepository userAstReltnRepository;

  private final UserObReltnRepository userObReltnRepository;

  private final UserDetailsRepository userDetailsRepository;
  private final OthersBusinessRepository othersBusinessRepository;

  private final JobConverter jobConverter;

  /**
   * Evaluates the Candidate related activites.
   *
   * @param keyCloakId the IAM id.
   * @return the {@link CandidateActivitiesResponse}.
   */
  @Transactional
  public CandidateActivitiesResponse retrieveCandidateActivities(String keyCloakId) {
    Verify.verifyNotNull(keyCloakId, "User Id cannot be null");
    final long completedAssessments = fetchCompletedAssessmentCount(keyCloakId);
    final long interviewCalls = fetchJobsInProgressCount(keyCloakId);
    return CandidateActivitiesResponse.builder()
        .completed(completedAssessments)
        .interviewCalls(interviewCalls)
        .jobsApplied(0l)
        .build();
  }

  @Transactional
  public List<OthersBusinessResponse> retrieveConsideredJobs(String keyCloakId) {
    Verify.verifyNotNull(keyCloakId, "User Id cannot be null");
    final List<UserObReltn> userObReltns = userObReltnRepository.findByUserId(keyCloakId);
    if (userObReltns.isEmpty()) {
      log.info("No result found");
    }
    final Set<Long> jobIds =
        filterWithHiringStatus(userObReltns, HiringStatus.IN_PROGRESS)
            .map(reltn -> reltn.getJobId())
            .collect(Collectors.toSet());
    final List<OthersBusiness> allJobs = othersBusinessRepository.findAllById(jobIds);
    return jobConverter.toResponse(allJobs);
  }

  @Transactional
  public AnalyticsResponse evaluateCandidateAssessments(String keyCloakId) {

    return null;
  }

  @Transactional
  public AnalyticsResponse retrieveHrDashBoard(String keyCloakId) {

    return null;
  }

  /**
   * Returns the analytics for a user.
   *
   * @param keyCloakId the IAM ID.
   * @return the {@link AnalyticsResponse}.
   */
  @Transactional
  public AnalyticsResponse retrieveDashBoardStatus(String keyCloakId) {
    Verify.verifyNotNull(keyCloakId, "User Id cannot be null");

    final UserProfile profile = userDetailsRepository.findByKeyCloakId(keyCloakId);
    AnalyticsResponse analyticsResponse = new AnalyticsResponse();

    if (UserRole.ROLE_CANDIDATE == profile.getRole()) {
      analyticsResponse.setCandidateDashboardStatus(evaluateCandidateAnalytics(keyCloakId));
      return analyticsResponse;
    }

    analyticsResponse.setHrDashboardStatus(evaluateHrAnalytics(keyCloakId));
    return analyticsResponse;
  }

  /**
   * Evaluates HR related analytics.
   *
   * @param keyCloakId the IAM id.
   * @return the {@link HrDashboardStatus}.
   */
  private HrDashboardStatus evaluateHrAnalytics(String keyCloakId) {
    List<UserObReltn> userObReltns = userObReltnRepository.findByUserId(keyCloakId);

    if (userObReltns.isEmpty()) {
      log.info("No Results found in Database");
    }

    final long hired = filterWithHiringStatus(userObReltns, HiringStatus.HIRED).count();
    final long yetToBeHired = filterWithHiringStatus(userObReltns, HiringStatus.NOT_HIRED).count();
    final long inProgress = filterWithHiringStatus(userObReltns, HiringStatus.IN_PROGRESS).count();

    HrDashboardStatus hrDashboardStatus = new HrDashboardStatus();
    hrDashboardStatus.setClosed(hired);
    hrDashboardStatus.setHiringInProgress(inProgress);
    hrDashboardStatus.setYetToBeHired(yetToBeHired);

    return hrDashboardStatus;
  }

  private long fetchJobsInProgressCount(String keyCloakId) {
    List<UserObReltn> userObReltns = userObReltnRepository.findByUserId(keyCloakId);
    if (userObReltns.isEmpty()) {
      log.info("No Results found in Database");
    }
    return filterWithHiringStatus(userObReltns, HiringStatus.IN_PROGRESS).count();
  }

  private long fetchCompletedAssessmentCount(String keyCloakId) {
    List<UserAstReltn> userAstReltns = userAstReltnRepository.findByUserId(keyCloakId);
    if (userAstReltns.isEmpty()) {
      log.info("No Results found in Database");
    }
    return countWithStatus(userAstReltns, COMPLETED);
  }

  /**
   * Evaluates the Candidate related analytics.
   *
   * @param keyCloakId the IAM id.
   * @return the {@link CandidateDashboardStatus}.
   */
  private CandidateDashboardStatus evaluateCandidateAnalytics(String keyCloakId) {
    List<UserAstReltn> userAstReltns = userAstReltnRepository.findByUserId(keyCloakId);

    if (userAstReltns.isEmpty()) {
      log.info("No Results found in Database");
    }

    final long completedAssessments = countWithStatus(userAstReltns, COMPLETED);
    final long blockedAssessments = countWithStatus(userAstReltns, BLOCKED);
    // final long failedAssessments = countWith(userAstReltns, FAILED);
    final long pendingAssessments = countWithStatus(userAstReltns, PENDING);
    final long passedAssessments = countWithStatus(userAstReltns, PASSED);

    CandidateDashboardStatus candidateDashboardStatus = new CandidateDashboardStatus();
    candidateDashboardStatus.setCompleted(completedAssessments);
    candidateDashboardStatus.setBlocked(blockedAssessments);
    candidateDashboardStatus.setTodo(pendingAssessments);
    candidateDashboardStatus.setPassed(passedAssessments);
    return candidateDashboardStatus;
  }

  /**
   * Helper method for Analytics.
   *
   * @param userAstReltns the list of {@link UserAstReltn}.
   * @param assessmentStatus {@link AssessmentStatus}.
   * @return the count of status matched elements.
   */
  private long countWithStatus(
      Collection<UserAstReltn> userAstReltns, AssessmentStatus assessmentStatus) {
    return userAstReltns.stream().filter(reltn -> assessmentStatus == reltn.getStatus()).count();
  }

  /**
   * Helper method for Analytics.
   *
   * @param userObReltns the list of {@link UserObReltn}
   * @param hiringStatus the {@link HiringStatus}
   * @return the count of status matched elements.
   */
  private Stream<UserObReltn> filterWithHiringStatus(
      Collection<UserObReltn> userObReltns, HiringStatus hiringStatus) {
    return userObReltns.stream().filter(reltn -> hiringStatus == reltn.getHiringStatus());
  }
}
