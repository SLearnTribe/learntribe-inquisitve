package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.AssessmentType;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Assessment Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public class AssessmentConverter {

  /**
   * Converts the {@link Assessment} to {@link AssessmentResponse}.
   *
   * @param assessment the {@link Assessment}
   * @return the {@link AssessmentResponse}
   */
  public AssessmentResponse toResponse(Assessment assessment) {
    AssessmentResponse response = new AssessmentResponse();
    response.setId(assessment.getId());
    response.setName(assessment.getName());
    response.setProgress(assessment.getProgress());
    response.setNumOfQuestions(assessment.getQuestions());

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
  public List<AssessmentResponse> toResponse(List<Assessment> assessmentList) {
    return assessmentList.stream().map(this::toResponse).collect(Collectors.toList());
  }
}
