package com.smilebat.learntribe.learntribeinquisitve.services;

import com.google.common.base.Verify;
import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.OthersBusinessRequest;
import com.smilebat.learntribe.inquisitve.UserAstReltnType;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.inquisitve.response.OthersBusinessResponse;
import com.smilebat.learntribe.learntribeclients.openai.OpenAiService;
import com.smilebat.learntribe.learntribeinquisitve.converters.AssessmentConverter;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.AssessmentRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.ChallengeRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.UserAstReltnRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import com.smilebat.learntribe.openai.OpenAiRequest;
import com.smilebat.learntribe.openai.response.Choice;
import com.smilebat.learntribe.openai.response.OpenAiResponse;
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

    try {
      userAstReltns = userAstReltnRepository.findByUserId(keyCloakId);
    } catch (Exception ex) {
      log.info("Assessments for User {} does not exist", keyCloakId);
      // ex.printStackTrace();
      return Collections.emptyList();
    }

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

    Assessment assessment = assessmentConverter.toEntity(request);
    assessment.setCreatedBy(userId);

    final OpenAiResponse completions = openAiService.getCompletions(new OpenAiRequest());

    final List<Choice> choices = completions.getChoices();

    if (choices == null || choices.isEmpty()) {
      log.info("Unable to create open ai completion text");
    }

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
    Set<Challenge> challenges = parseCompletedText(completedText);

    assessment.setChallenges(challenges);
    assessmentRepository.save(assessment);

    UserAstReltn userAstReltnForHr = createUserAstReltn(userId, assessment);
    // UserAstReltn userAstReltnForCandidate = createUserAstReltn(userId, assessment);
    userAstReltnRepository.saveAll(List.of(userAstReltnForHr));

    return assessmentConverter.toResponse(assessment);
  }

  /**
   * Parses the text completion for query extractions.
   *
   * @param str the completed open ai text.
   * @return the Set of {@link Challenge}.
   */
  private Set<Challenge> parseCompletedText(String str) {
    String[] arr = str.split("\n\n");
    Set<Challenge> challenges = new HashSet<>(15);
    int index = 1;
    int arrLen = arr.length;
    while (index < arrLen) {
      Challenge challenge = new Challenge();
      challenge.setQuestion(arr[index++]);
      challenge.setOptions(arr[index++].split("\n"));
      challenge.setAnswer(arr[index++]);
      challenges.add(challenge);
    }
    return challenges;
  }

  /**
   * Creates a User Job relation object.
   *
   * @param userId the keyCloak user Id
   * @param assessment the {@link OthersBusiness}
   * @return the {@link UserObReltn}
   */
  private UserAstReltn createUserAstReltn(String userId, Assessment assessment) {
    UserAstReltn userAstReltn = new UserAstReltn();
    userAstReltn.setUserId(userId);
    userAstReltn.setAssessmentId(assessment.getId());
    userAstReltn.setUserAstReltnType(UserAstReltnType.CREATED);
    return userAstReltn;
  }
}
