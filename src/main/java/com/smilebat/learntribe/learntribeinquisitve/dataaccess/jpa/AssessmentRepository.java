package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

  /**
   * Finds the Assessments mapped to user based on user profile id.
   *
   * @param userProfileId the profile id
   * @return the List of {@link Assessment}
   */
  List<Assessment> findByUserId(Long userProfileId);
}
