package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface UserAstReltnRepository extends JpaRepository<UserAstReltn, Long> {

  /**
   * Finds the Assessments mapped to user based on user profile id.
   *
   * @param keyCloakId the profile id
   * @return the List of {@link Assessment}
   */
  @Query(value = "SELECT * FROM usr_ast_reltn ua WHERE ua.user_id = :userId", nativeQuery = true)
  List<UserAstReltn> findByUserId(@Param("userId") String keyCloakId);

  @Query(
      value =
          "SELECT * FROM usr_ast_reltn ua WHERE ua.user_id = :userId and ua.assessment_id = :assessmentId",
      nativeQuery = true)
  List<UserAstReltn> findByUserAstReltn(
      @Param("userId") String keyCloakId, @Param("assessmentId") Long assessmentId);
}
