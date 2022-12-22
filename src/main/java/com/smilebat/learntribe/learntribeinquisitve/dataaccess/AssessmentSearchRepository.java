package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.enums.AssessmentStatus;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import java.util.List;
import org.springframework.data.domain.Pageable;

/** Returns Data Access by Assessment Repo */
public interface AssessmentSearchRepository {

  /**
   * Searches for keyword using lucene query in db.
   *
   * @param keyword the search word
   * @param keyCloakId the IAM user id
   * @param filters the {@link AssessmentStatus} filters.
   * @param pageable the {@link Pageable}.
   * @return the List of {@link UserAstReltn}
   * @throws InterruptedException on db error
   */
  List<UserAstReltn> search(String keyword, String[] filters, String keyCloakId, Pageable pageable)
      throws InterruptedException;
}
