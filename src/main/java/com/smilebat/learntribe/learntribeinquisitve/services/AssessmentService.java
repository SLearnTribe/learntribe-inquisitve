package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
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
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.AssessmentRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.ChallengeRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserAstReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserObReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  /**
   * Retrieves user & skill related assessments.
   *
   * @param keyCloakId the ID provided by IAM (keycloak)
   * @return the List of {@link AssessmentResponse}
   */
  @Transactional
  @Nullable
  public List<AssessmentResponse> retrieveAssessments(String keyCloakId) {
    Verify.verifyNotNull(keyCloakId, "User Keycloak Id cannnot be null");

    log.info("Fetching Assessments for User {}", keyCloakId);

    List<UserAstReltn> userAstReltns = null;

    userAstReltns = userAstReltnRepository.findByUserId(keyCloakId);

    if (userAstReltns == null || userAstReltns.isEmpty()) {
      log.info("Assessments for User {} does not exist", keyCloakId);

      // return default assessments from oepn ai based on his skills.

      return Collections.emptyList();
    }

    final List<Long> assessmentIds =
        userAstReltns.stream().map(UserAstReltn::getAssessmentId).collect(Collectors.toList());

    List<Assessment> assessments = assessmentRepository.findAllById(assessmentIds);

    if (assessments == null || assessments.isEmpty()) {
      log.info("No Existing Assessments found for the current user");
      // assessments = Assessment.generateMockAssessments();
      // get default assessments from oepn ai based on his skills.

      return Collections.emptyList();
    }
    return assessmentConverter.toResponse(assessments);
  }

  /**
   * Creates a assessment as per the requirements.
   *
   * @param userId the keycloak id of the recruiter.
   * @param request the {@link OthersBusinessRequest}.
   * @return the {@link OthersBusinessResponse}.
   */
  @Transactional
  public AssessmentResponse createAssessment(String userId, AssessmentRequest request) {
    Verify.verifyNotNull(userId, "User Id cannot be null");
    Verify.verifyNotNull(request, "Job Request cannot be null");

    final String title = request.getTitle();
    final String candidateId = request.getCreatedFor();
    final Long relatedJobId = request.getRelatedJobId();

    List<Assessment> hrAssessments = assessmentRepository.findByTitle(userId, title);

    if (hrAssessments != null && !hrAssessments.isEmpty()) {
      Assessment assessment = hrAssessments.get(0);
      assignExistingAssessments(candidateId, assessment);
      return assessmentConverter.toResponse(assessment);
    }

    /*Create a fresh assignment*/

    Assessment assessment = assessmentConverter.toEntity(request);
    assessment.setCreatedBy(userId);
    assessmentRepository.save(assessment);
    Long assessmentId = assessment.getId();

    createFreshAssessment(assessment);
    createUserAssessmentRelation(userId, candidateId, assessmentId);
    createUserJobReltn(candidateId, relatedJobId);

    return assessmentConverter.toResponse(assessment);
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

    userAstReltnRepository.saveAll(List.of(userAstReltnForHr, userAstReltnForCandidate));
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
      challenge.setQuestion(arr[index++]);
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
