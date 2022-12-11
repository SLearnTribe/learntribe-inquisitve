package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa;

import com.smilebat.learntribe.inquisitve.AssessmentStatus;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.AssessmentSearchRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.UserAstReltn;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link AssessmentSearchRepository}
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Service
public class AssessmentSearchRepositoryImpl implements AssessmentSearchRepository {

  @PersistenceContext private EntityManager em;

  @Override
  public List<UserAstReltn> search(
      String keyword, String[] filters, String keyCloakId, Pageable pageable)
      throws InterruptedException {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
    fullTextEntityManager.createIndexer().startAndWait();
    QueryBuilder queryBuilder =
        fullTextEntityManager
            .getSearchFactory()
            .buildQueryBuilder()
            .forEntity(UserAstReltn.class)
            .get();

    Query keywordQuery =
        queryBuilder.keyword().onFields("assessmentTitle").matching(keyword).createQuery();
    Query userIdQuery = queryBuilder.keyword().onField("userId").matching(keyCloakId).createQuery();
    MustJunction intermediateCriteria = queryBuilder.bool().must(keywordQuery).must(userIdQuery);
    if (filters.length == 1) {
      intermediateCriteria.must(
          queryBuilder
              .keyword()
              .onField("status")
              .matching(AssessmentStatus.valueOf(filters[0]))
              .createQuery());
    }
    Query finalQuery = intermediateCriteria.createQuery();

    // wrap Lucene query in a javax.persistence.Query
    FullTextQuery jpaQuery =
        fullTextEntityManager.createFullTextQuery(finalQuery, UserAstReltn.class);
    jpaQuery.initializeObjectsWith(
        ObjectLookupMethod.SECOND_LEVEL_CACHE, DatabaseRetrievalMethod.QUERY);
    jpaQuery.setMaxResults(pageable.getPageSize());
    jpaQuery.setFirstResult(pageable.getPageNumber());

    // execute search
    return jpaQuery.getResultList();
  }
}
