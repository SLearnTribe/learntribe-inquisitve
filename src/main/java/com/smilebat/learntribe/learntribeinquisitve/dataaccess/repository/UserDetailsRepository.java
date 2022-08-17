package com.smilebat.learntribe.learntribeinquisitve.dataaccess.repository;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Returns Data Access by USer Repo */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {}
