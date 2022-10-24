package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface AssessmentRepository extends PagingAndSortingRepository<Assessment, Long> {

  /**
   * Queries assessments based on title and user id.
   *
   * @param userId the IAM userId
   * @param title the title
   * @param pageable pageable object for pagination
   * @return the List of {@link Assessment}.
   */
  @Query(
      value = "SELECT * FROM assessment a WHERE a.title like :title and a.created_by = :userId",
      nativeQuery = true)
  List<Assessment> findByTitle(
      @Param("userId") String userId, @Param("title") String title, Pageable pageable);

  /**
   * Queries assessments based on Assessment ID.
   *
   * @param id Assessment ID
   * @return assessment.
   */
  @Query(value = "SELECT * FROM assessment a WHERE a.id = :id", nativeQuery = true)
  Assessment findByAssessmentId(@Param("id") Long id);

  /**
   * Queries assessments based on title and user id.
   *
   * @param assessmentIds array of Assessment IDs
   * @param pageable pageable object for pagination
   * @return the List of {@link Assessment}.
   */
  @Query(value = "SELECT * FROM assessment where id in :ids", nativeQuery = true)
  List<Assessment> findAllByIds(@Param("ids") Long[] assessmentIds, Pageable pageable);
}
