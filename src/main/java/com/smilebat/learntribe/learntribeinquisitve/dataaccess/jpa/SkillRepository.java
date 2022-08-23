package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;
import java.util.Collection;
import java.util.List;

/** Returns Data Access by Skill Repo */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query(value = "SELECT S FROM SKILL AS S WHERE S.SKILL_NAME IN :skillNames AND S.USER_DETAILS_ID = :userId", nativeQuery = true)
    List<Skill> findAllBySkillName(@Param("skillNames") Collection<String> skillNames,@Param("userId") Long userId);
}
