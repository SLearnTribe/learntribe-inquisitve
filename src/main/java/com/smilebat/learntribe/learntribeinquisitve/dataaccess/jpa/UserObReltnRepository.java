package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserObReltn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface UserObReltnRepository extends JpaRepository<UserObReltn, Long> {

  /**
   * Finds the Assessments mapped to user based on user profile id.
   *
   * @param keyCloakId the profile id
   * @return the List of {@link Assessment}
   */
  @Query(value = "SELECT * FROM usr_ob_reltn uob WHERE uob.user_id = :userId", nativeQuery = true)
  List<UserObReltn> findByUserId(@Param("userId") String keyCloakId);
}
