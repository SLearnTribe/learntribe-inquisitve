package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.UserProfileSearchRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserProfile;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserProfileSearchRepository}
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
public class UserProfileSearchRepositoryImpl implements UserProfileSearchRepository {

  @PersistenceContext private EntityManager em;

  @Override
  public List<UserProfile> search(String keyword, Pageable pageable) throws InterruptedException {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
    fullTextEntityManager.createIndexer().startAndWait();

    QueryBuilder queryBuilder =
        fullTextEntityManager
            .getSearchFactory()
            .buildQueryBuilder()
            .forEntity(UserProfile.class)
            .get();
    org.apache.lucene.search.Query luceneQuery =
        queryBuilder
            .keyword()
            .onFields("skills", "about", "country")
            .matching(keyword)
            .createQuery();

    // wrap Lucene query in a javax.persistence.Query
    FullTextQuery jpaQuery =
        fullTextEntityManager.createFullTextQuery(luceneQuery, UserProfile.class);

    jpaQuery.setMaxResults(pageable.getPageSize());
    jpaQuery.setFirstResult(pageable.getPageNumber());

    // execute search
    return jpaQuery.getResultList();
  }
}
