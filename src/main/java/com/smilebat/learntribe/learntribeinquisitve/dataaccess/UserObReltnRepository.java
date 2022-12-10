package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Jobs Repo */
@Repository
public interface UserObReltnRepository extends JpaRepository<UserObReltn, Long> {

  /**
   * Finds the Jobs mapped to user based on user profile id.
   *
   * @param keyCloakId the profile id
   * @return the List of {@link Assessment}
   */
  @Query(value = "SELECT * FROM usr_ob_reltn uob WHERE uob.user_id = :userId", nativeQuery = true)
  List<UserObReltn> findByUserId(@Param("userId") String keyCloakId);

  /**
   * Finds the user related job id.
   *
   * @param keyCloakId the profile id
   * @param jobId the job id
   * @return the List of {@link Assessment}
   */
  @Query(
      value =
          "SELECT * FROM usr_ob_reltn uob WHERE uob.user_id = :userIAMId and uob.job_id = :relateJobId",
      nativeQuery = true)
  UserObReltn findByRelatedJobId(
      @Param("userIAMId") String keyCloakId, @Param("relateJobId") Long jobId);

  /**
   * Finds the Jobs mapped to user based on user profile id and Hiring status.
   *
   * @param keyCloakId the profile id
   * @param filters the hiring status filters
   * @return the List of {@link Assessment}
   */
  @Query(
      value =
          "SELECT * FROM usr_ob_reltn uob WHERE uob.user_id = :userIAMId and uob.hiring_status in :filters",
      nativeQuery = true)
  List<UserObReltn> findByUserIdAndStatus(
      @Param("userIAMId") String keyCloakId, @Param("filters") String[] filters);

  /**
   * Finds the Jobs mapped to user based on user profile id and Hiring status.
   *
   * @param keyCloakId the profile id
   * @param filters the hiring status filters
   * @return the List of {@link Assessment}
   */
  @Query(
      value =
          "SELECT COUNT(*) FROM usr_ob_reltn uob WHERE uob.user_id = :userId and uob.hiring_status in :filters",
      nativeQuery = true)
  Long countByUserIdAndStatus(
      @Param("userId") String keyCloakId, @Param("filters") String[] filters);

  /**
   * Finds the Jobs mapped to user based on job id and Hiring status.
   *
   * @param jobId the actual job id.
   * @param filters the hiring status filters
   * @return the List of {@link Assessment}
   */
  @Query(
      value =
          "SELECT COUNT(*) FROM usr_ob_reltn uob WHERE uob.id = :jobId and uob.hiring_status in :filters",
      nativeQuery = true)
  Long countByJobHiringStatus(@Param("jobId") Long jobId, @Param("filters") String[] filters);
}
