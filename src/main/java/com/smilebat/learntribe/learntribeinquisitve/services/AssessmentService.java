package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.AssessmentDifficulty;
import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.AssessmentStatus;
import com.smilebat.learntribe.inquisitve.HiringStatus;
import com.smilebat.learntribe.inquisitve.OthersBusinessRequest;
import com.smilebat.learntribe.inquisitve.UserAstReltnType;
import com.smilebat.learntribe.inquisitve.UserObReltnType;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeclients.openai.OpenAiService;
import com.smilebat.learntribe.learntribeinquisitve.converters.AssessmentConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.AssessmentRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.ChallengeRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserAstReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserDetailsRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserObReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Assessment Service to hold the business logic.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AssessmentService {

  private final AssessmentRepository assessmentRepository;
  private final AssessmentConverter assessmentConverter;
  private final UserAstReltnRepository userAstReltnRepository;
  private final UserObReltnRepository userObReltnRepository;
  private final ChallengeRepository challengeRepository;

  private final OpenAiService openAiService;

  private final UserDetailsRepository userDetailsRepository;

  private static final String[] ASSESSMENT_STATUS_FILTERS =
      Arrays.stream(AssessmentStatus.values())
          .map(AssessmentStatus::name)
          .toArray(s -> new String[s]);

  /** Assessment pagination concept builder. */
  @Getter
  @Setter
  @Builder
  public static class PageableAssessmentRequest {
    private String keyCloakId;
    private String[] filters;
    private Pageable paging;
  }

  /**
   * Retrieves user & skill related assessments.
   *
   * @param request the {@link PageableAssessmentRequest} the ID provided by IAM (keycloak)
   * @return the List of {@link AssessmentResponse}
   */
  @Transactional
  @Nullable
  public List<AssessmentResponse> retrieveUserAssessments(PageableAssessmentRequest request) {
    String keyCloakId = request.getKeyCloakId();
    Verify.verifyNotNull(keyCloakId, "User Keycloak Id cannnot be null");
    log.info("Fetching Assessments for User {}", keyCloakId);
    Pageable paging = request.getPaging();
    String[] filters = evaluateAssessmentStatusFilters(request);

    List<UserAstReltn> userAstReltns =
        userAstReltnRepository.findByUserIdAndFilter(keyCloakId, filters);

    if (userAstReltns == null || userAstReltns.isEmpty()) {
      log.info("Assessments for User {} does not exist", keyCloakId);
      final List<Assessment> systemGeneratedAssessments = createDefaultAssessments(keyCloakId);
      return assessmentConverter.toResponse(systemGeneratedAssessments);
    }

    final Long[] assessmentIds =
        userAstReltns.stream().map(UserAstReltn::getAssessmentId).toArray(s -> new Long[s]);
    List<Assessment> assessments = assessmentRepository.findAllByIds(assessmentIds, paging);
    return assessmentConverter.toResponse(assessments);
  }

  /**
   * Evaluates Assessment Status filters.
   *
   * @param request the {@link PageableAssessmentRequest}
   * @return the array of {@link String} filters
   */
  private String[] evaluateAssessmentStatusFilters(PageableAssessmentRequest request) {
    String[] filters = request.getFilters();
    return filters != null && filters.length > 0 ? filters : ASSESSMENT_STATUS_FILTERS;
  }

  /**
   * Generate a default assessment based on his skills.
   *
   * @param candidateId the candidate for whom the assessment is assigned for.
   * @return List of {@link Assessment}.
   */
  @Transactional
  private List<Assessment> createDefaultAssessments(String candidateId) {
    log.info("Evaluating User {} Skills", candidateId);
    UserProfile userProfile = userDetailsRepository.findByKeyCloakId(candidateId);
    if (userProfile == null) {
      return Collections.emptyList();
    }
    Set<String> userSkills = evaluateUserSkills(userProfile);

    if (userSkills.isEmpty()) {
      return Collections.emptyList();
    }

    log.info("Initializing default assessments for User {}", candidateId);

    List<Assessment> defaultAssessments =
        userSkills.stream().map(this::createSystemAssessment).collect(Collectors.toList());

    assessmentRepository.saveAll(defaultAssessments);

    for (Assessment assessment : defaultAssessments) {
      Long assessmentId = assessment.getId();
      log.info("Creating fresh assessment {} for User {}", assessmentId, candidateId);
      createFreshAssessment(assessment);
      createUserAssessmentRelation("SYSTEM", candidateId, assessmentId);
    }

    return defaultAssessments;
  }

  private Set<String> evaluateUserSkills(UserProfile userProfile) {
    String skills = userProfile.getSkills();
    if (skills == null || skills.isEmpty()) {
      return Collections.emptySet();
    }
    return Arrays.stream(skills.split(",")).collect(Collectors.toSet());
  }

  private Assessment createSystemAssessment(String skill) {
    Assessment assessment = new Assessment();
    assessment.setTitle(skill);
    assessment.setDifficulty(AssessmentDifficulty.EASY.name());
    assessment.setDescription("Recommended");
    assessment.setCreatedBy("SYSTEM");
    return assessment;
  }

  /**
   * Retrieves user & skill related assessments.
   *
   * @param assessmentId the ID provided by IAM (keycloak)
   * @return AssessmentResponse
   */
  @Transactional
  public AssessmentResponse retrieveAssessment(Long assessmentId) {
    Verify.verifyNotNull(assessmentId, "Assessment ID cannnot be null");

    log.info("Fetching Assessments with id {}", assessmentId);

    Assessment assessment = assessmentRepository.findByAssessmentId(assessmentId);

    if (assessment == null) {
      log.info("No Assessment found");
      return new AssessmentResponse();
    }
    return assessmentConverter.toResponse(assessment);
  }

  /**
   * Creates a assessment as per the requirements.
   *
   * @param userId the keycloak id of the recruiter.
   * @param request the {@link OthersBusinessRequest}
   * @return the {@link OthersBusinessResponse}.
   */
  @Transactional
  public boolean createAssessment(String userId, AssessmentRequest request) {
    Verify.verifyNotNull(userId, "User Id cannot be null");
    Verify.verifyNotNull(request, "Job Request cannot be null");

    final String title = request.getTitle();
    final String candidateId = request.getCreatedFor();
    final Long relatedJobId = request.getRelatedJobId();
    List<Assessment> hrAssessments = assessmentRepository.findByTitle(userId, title);

    if (hrAssessments != null && !hrAssessments.isEmpty()) {
      Assessment assessment = hrAssessments.get(0);
      assignExistingAssessments(candidateId, assessment);
      // return assessmentConverter.toResponse(assessment);
      return true;
    }

    /*Create a fresh assignment*/

    Assessment assessment = assessmentConverter.toEntity(request);
    assessment.setCreatedBy(userId);
    assessmentRepository.save(assessment);
    Long assessmentId = assessment.getId();

    createFreshAssessment(assessment);
    createUserAssessmentRelation(userId, candidateId, assessmentId);
    createUserJobReltn(candidateId, relatedJobId);

    // return assessmentConverter.toResponse(assessment);
    return true;
  }

  @Transactional
  private void createUserJobReltn(String candidateId, Long jobId) {
    UserObReltn userObReltn = createUserObReltn(candidateId, jobId);
    userObReltnRepository.save(userObReltn);
  }

  private UserObReltn createUserObReltn(String candidateId, Long jobId) {
    UserObReltn userObReltn = new UserObReltn();
    userObReltn.setUserObReltn(UserObReltnType.CANDIDATE);
    userObReltn.setHiringStatus(HiringStatus.IN_PROGRESS);
    userObReltn.setUserId(candidateId);
    userObReltn.setJobId(jobId);
    return userObReltn;
  }

  @Transactional
  private void createUserAssessmentRelation(String userId, String candidateId, Long assessmentId) {
    UserAstReltn userAstReltnForHr = createUserAstReltnForHr(userId, assessmentId);
    UserAstReltn userAstReltnForCandidate =
        createUserAstReltnForCandidate(candidateId, assessmentId);
    List<UserAstReltn> userAstReltns = List.of(userAstReltnForHr, userAstReltnForCandidate);
    userAstReltnRepository.saveAll(userAstReltns);
  }

  @Transactional
  private void assignExistingAssessments(String candidateId, Assessment hrAssessment) {
    final Long hrAssessmentId = hrAssessment.getId();
    List<UserAstReltn> userAstReltns =
        userAstReltnRepository.findByUserAstReltn(candidateId, hrAssessmentId);
    if (userAstReltns.isEmpty()) {
      UserAstReltn userAstReltnForCandidate =
          createUserAstReltnForCandidate(candidateId, hrAssessmentId);
      userAstReltnRepository.save(userAstReltnForCandidate);
    }
  }

  @Transactional
  private void createFreshAssessment(Assessment assessment) {
    //    final OpenAiResponse completions = openAiService.getCompletions(new OpenAiRequest());
    //
    //    final List<Choice> choices = completions.getChoices();
    //
    //    if (choices == null || choices.isEmpty()) {
    //      log.info("Unable to create open ai completion text");
    //    }

    //    Choice choice = choices.get(0);
    //    String completedText = choice.getText();
    String completedText =
        "\n\n1. What is the most important feature of Java?\n\na. Platform independent\nb. "
            + "Object oriented\nc. Simple\nd. Secure\n\nAnswer: "
            + "a. Platform independent\n\n2. What is the default value of a local variable?\n\na. 0\nb. null\nc. "
            + "Compile time error\nd. "
            + "Runtime error\n\nAnswer: c. Compile time error\n\n3. Which of the following is not a keyword in "
            + "java?\n\na. native\nb. volatile\nc. public\nd. strictfp\n\nAnswer: a. native\n\n4. "
            + "Which of the following is not a valid identifier in java?\n\na. _name\nb. "
            + "$age\nc. #value\nd. name%\n\nAnswer: c. #value\n\n5. "
            + "What is the range of a char data type in java?\n\na. "
            + "-128 to 127\nb. 0 to 255\nc. -32768 to 32767\nd. Unicode\n\nAnswer: Unknown";
    Set<Challenge> challenges = parseCompletedText(completedText, assessment);
    assessment.setChallenges(challenges);

    challengeRepository.saveAll(challenges);
  }

  /**
   * Parses the text completion for query extractions.
   *
   * @param str the completed open ai text.
   * @param assessment the {@link Assessment} entity.
   * @return the Set of {@link Challenge}.
   */
  private Set<Challenge> parseCompletedText(String str, Assessment assessment) {
    String[] arr = str.split("\n\n");
    Set<Challenge> challenges = new HashSet<>(15);
    int index = 1;
    int arrLen = arr.length;
    while (index < arrLen) {
      Challenge challenge = new Challenge();
      challenge.setQuestion(arr[index++].substring(3));
      challenge.setOptions(arr[index++].split("\n"));
      challenge.setAnswer(arr[index++]);
      challenge.setAssessmentInfo(assessment);
      challenges.add(challenge);
    }
    return challenges;
  }

  /**
   * Creates a User Assessment relation object for HR.
   *
   * @param userId the keyCloak user Id
   * @param assessmentId the assessmentId
   * @return the {@link UserAstReltn}
   */
  private UserAstReltn createUserAstReltnForHr(String userId, Long assessmentId) {
    UserAstReltn userAstReltn = new UserAstReltn();
    userAstReltn.setUserId(userId);
    userAstReltn.setAssessmentId(assessmentId);
    userAstReltn.setStatus(AssessmentStatus.DEFAULT);
    userAstReltn.setUserAstReltnType(UserAstReltnType.CREATED);
    return userAstReltn;
  }

  /**
   * Creates a User Assessment relation object for HR.
   *
   * @param userId the keyCloak user Id
   * @param assessmentId the assessmentId
   * @return the {@link UserAstReltn}
   */
  private UserAstReltn createUserAstReltnForCandidate(String userId, Long assessmentId) {
    UserAstReltn userAstReltn = new UserAstReltn();
    userAstReltn.setUserId(userId);
    userAstReltn.setAssessmentId(assessmentId);
    userAstReltn.setStatus(AssessmentStatus.PENDING);
    userAstReltn.setUserAstReltnType(UserAstReltnType.ASSIGNED);
    return userAstReltn;
  }
}
