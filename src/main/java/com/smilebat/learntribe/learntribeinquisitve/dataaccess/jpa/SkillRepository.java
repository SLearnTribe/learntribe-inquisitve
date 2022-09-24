package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Skill Repo */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

  /**
   * Retrieves skills based on skill names and user profile id.
   *
   * @param skillName the {@link String}.
   * @return the List of {@link Skill}.
   */
  @Query(
      value = "SELECT ID, SKILL_NAME, USER_DETAILS_ID FROM SKILL WHERE SKILL_NAME = :skillName",
      nativeQuery = true)
  List<Skill> findBySkillName(@Param("skillName") String skillName);

  /**
   * Retrieves skills based on user profile id.
   *
   * @param userId the {@link Long}.
   * @return the List of {@link Skill}.
   */
  @Query(
      value = "SELECT ID, SKILL_NAME, USER_DETAILS_ID FROM SKILL WHERE USER_DETAILS_ID = :userId",
      nativeQuery = true)
  List<Skill> findByUserId(@Param("userId") Long userId);
}
