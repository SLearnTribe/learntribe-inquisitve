package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.response.HrHiringsResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Analytics Converter to map between Entities, Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
@RequiredArgsConstructor
public class AnalyticsConverter {

  DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

  /**
   * Converts {@link OthersBusiness} to {@link HrHiringsResponse}.
   *
   * @param othersBusiness the {@link OthersBusiness}.
   * @param jobCount the count of the josb fetched.
   * @return the {@link HrHiringsResponse}.
   */
  public HrHiringsResponse toResponse(OthersBusiness othersBusiness, Long jobCount) {
    HrHiringsResponse response = new HrHiringsResponse();
    response.setJobTitle(othersBusiness.getTitle());
    response.setSkills(othersBusiness.getRequiredSkills());
    response.setJobPostedOn(dateFormat.format(othersBusiness.getCreatedDate()));
    response.setJobStatus(othersBusiness.getStatus().name());
    response.setJobCount(jobCount);
    return response;
  }
}
