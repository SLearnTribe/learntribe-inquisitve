package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.AssessmentType;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assessment Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
@RequiredArgsConstructor
public final class AssessmentConverter {

  private final ChallengeConverter challengeConverter;

  /**
   * Converts the {@link AssessmentRequest} to {@link Assessment}.
   *
   * @param request the {@link AssessmentRequest}
   * @return the {@link Assessment}
   */
  public Assessment toEntity(AssessmentRequest request) {
    Assessment assessment = new Assessment();
    assessment.setDifficulty(request.getDifficulty());
    assessment.setCreatedBy(request.getAssignedBy());
    return assessment;
  }

  /**
   * Converts the {@link Assessment} to {@link AssessmentResponse}.
   *
   * @param assessment the {@link Assessment}
   * @return the {@link AssessmentResponse}
   */
  public AssessmentResponse toResponse(Assessment assessment) {
    AssessmentResponse response = new AssessmentResponse();
    response.setId(assessment.getId());
    response.setTitle(assessment.getTitle());
    response.setProgress(assessment.getProgress());
    response.setNumOfQuestions(assessment.getQuestions());
    response.setDescription(assessment.getDescription());
    response.setDifficulty(assessment.getDifficulty().name());
    response.setStatus(assessment.getStatus());
    List<Challenge> challenges = new ArrayList<>(assessment.getChallenges());
    response.setChallengeResponses(challengeConverter.toResponse(challenges));

    // response.setDescription();

    AssessmentType assessmentType = assessment.getType();
    if (assessmentType != null) {
      response.setType(assessment.getType().toString());
    }

    return response;
  }

  /**
   * Converts the List of {@link Assessment} to List of {@link AssessmentResponse}.
   *
   * @param assessmentList the List of {@link Assessment}
   * @return the List of {@link AssessmentResponse}
   */
  public List<AssessmentResponse> toResponse(Collection<Assessment> assessmentList) {
    return assessmentList.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
