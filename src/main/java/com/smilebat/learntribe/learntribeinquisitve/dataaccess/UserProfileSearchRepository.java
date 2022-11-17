package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.List;
import org.springframework.data.domain.Pageable;

/** Returns Data Access by User Profile Repo */
public interface UserProfileSearchRepository {

  /**
   * Searches for keyword using lucene query in db.
   *
   * @param keyword the search word
   * @param pageable the {@link Pageable}
   * @return the List of {@link UserProfile}
   * @throws InterruptedException on db error
   */
  List<UserProfile> search(String keyword, Pageable pageable) throws InterruptedException;
}
