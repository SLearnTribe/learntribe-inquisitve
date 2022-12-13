package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.EmploymentType;
import com.smilebat.learntribe.inquisitve.JobStatus;
import com.smilebat.learntribe.inquisitve.response.HrHiringsResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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
    Date createdDate = othersBusiness.getCreatedDate();
    if (createdDate != null) {

      response.setJobPostedOn(dateFormat.format(othersBusiness.getCreatedDate()));
    }

    JobStatus status = othersBusiness.getStatus();
    if (status != null) {

      response.setJobStatus(status.name());
    }
    response.setJobCount(jobCount);
    response.setBusinessName(othersBusiness.getBusinessName());
    EmploymentType type = othersBusiness.getEmploymentType();
    if (type != null) {
      response.setEmploymentType(type.name());
    }
    return response;
  }
}
