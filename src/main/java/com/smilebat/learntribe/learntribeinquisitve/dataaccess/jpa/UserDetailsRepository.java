package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by User Repo */
@Repository
public interface UserDetailsRepository extends PagingAndSortingRepository<UserProfile, Long> {

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
   * @param pageable pageable object for pagination
   * @return the List of {@link UserProfile}
   */
  @Query(value = "SELECT * FROM USER_PROFILE WHERE KEY_CLOAK_ID is not null", nativeQuery = true)
  List<UserProfile> findAllUsers(Pageable pageable);

  /**
   * Finds the profile based on skill.
   *
   * @param pageable object for pageination.
   * @param skill skill needed in the candidate.
   * @return the List of {@link UserProfile}
   */
  @Query(value = "FROM UserProfile A WHERE A.skills like %:skill%", nativeQuery = false)
  List<UserProfile> findBySkills(@Param("skill") String skill, Pageable pageable);
}
