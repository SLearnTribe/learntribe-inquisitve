package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
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
  UserProfile findByKeyCloakId(String keycloakId);
}
