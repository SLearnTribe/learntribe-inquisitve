package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

  /**
   * Queries assessments based on title and user id.
   *
   * @param userId the IAM userId
   * @param title the title
   * @return the List of {@link Assessment}.
   */
  @Query(
      value = "SELECT * FROM assessment a WHERE a.title like :title and a.created_by = :userId",
      nativeQuery = true)
  List<Assessment> findByTitle(@Param("userId") String userId, @Param("title") String title);
}
