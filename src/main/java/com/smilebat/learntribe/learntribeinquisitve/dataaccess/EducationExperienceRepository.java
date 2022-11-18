package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.EducationExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Educational Experience Repo */
@Repository
public interface EducationExperienceRepository extends JpaRepository<EducationExperience, Long> {}
