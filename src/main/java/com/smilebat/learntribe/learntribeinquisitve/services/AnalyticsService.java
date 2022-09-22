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
import com.smilebat.learntribe.inquisitve.response.CandidateDashboardStatus;
import com.smilebat.learntribe.inquisitve.response.HrDashboardStatus;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserAstReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserObReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Collection;
import java.util.List;
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

  /**
   * Returns the analytics for a user.
   *
   * @param keyCloakId the IAM ID.
   * @return the {@link AnalyticsResponse}.
   */
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

    final long hired = countWithReltnType(userObReltns, HiringStatus.HIRED);
    final long yetToBeHired = countWithReltnType(userObReltns, HiringStatus.NOT_HIRED);
    final long inProgress = countWithReltnType(userObReltns, HiringStatus.IN_PROGRESS);

    HrDashboardStatus hrDashboardStatus = new HrDashboardStatus();
    hrDashboardStatus.setClosed(hired);
    hrDashboardStatus.setHiringInProgress(inProgress);
    hrDashboardStatus.setYetToBeHired(yetToBeHired);

    return hrDashboardStatus;
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
  private long countWithReltnType(Collection<UserObReltn> userObReltns, HiringStatus hiringStatus) {
    return userObReltns.stream().filter(reltn -> hiringStatus == reltn.getHiringStatus()).count();
  }
}
