package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Returns Data Access by Assessment Repo */
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {}
