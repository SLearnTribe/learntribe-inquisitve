package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by User Repo */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserProfile, Long> {

  /**
   * Finds the profile based on IAM user id.
   *
   * @param keycloakId the IAM user id.
   * @return the {@link UserProfile}
   */
  @Query(
      value = "SELECT * FROM USER_PROFILE a WHERE a.key_cloak_id = :keyCloakId",
      nativeQuery = true)
  UserProfile findByKeyCloakId(@Param("keyCloakId") String keycloakId);

  /**
   * Finds all valid user profiles
   *
   * @return the List of {@link UserProfile}
   */
  @Query(value = "SELECT * FROM USER_PROFILE WHERE KEY_CLOAK_ID is not null", nativeQuery = true)
  List<UserProfile> findAll();
}
