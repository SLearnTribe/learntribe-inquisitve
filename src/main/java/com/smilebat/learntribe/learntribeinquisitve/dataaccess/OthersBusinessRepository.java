package com.smilebat.learntribe.learntribeinquisitve.dataaccess;

import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.OthersBusiness;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Returns Data Access by OB Repo */
@Repository
public interface OthersBusinessRepository extends PagingAndSortingRepository<OthersBusiness, Long> {
  /**
   * Finds the Jobs mapped to user based on user profile id.
   *
   * @param keyCloakId the profile id
   * @param pageable the {@link Pageable} required for pagination.
   * @return the List of {@link Assessment}
   */
  @Query(
      value = "SELECT * FROM OTHERS_BUSINESS ob WHERE ob.created_by = :userId",
      nativeQuery = true)
  List<OthersBusiness> findByUserId(@Param("userId") String keyCloakId, Pageable pageable);

  /**
   * Finds the Jobs mapped to user based on user profile id and matching date.
   *
   * @param keyCloakId the profile id
   * @param pageable the {@link Pageable} required for pagination.
   * @return the List of {@link Assessment}
   */
  @Query(
      value =
          "SELECT * FROM OTHERS_BUSINESS ob WHERE ob.created_by = :userId and ob.created_date >= CURRENT_DATE - 30",
      nativeQuery = true)
  List<OthersBusiness> findByUserIdAndCurrentDate(
      @Param("userId") String keyCloakId, Pageable pageable);

  /**
   * Finds the Jobs mapped to user based on user profile id.
   *
   * @param pageable the {@link Pageable} required for pagination.
   * @param jobId list of job id
   * @return the List of {@link Assessment}
   */
  @Query(value = "SELECT * FROM OTHERS_BUSINESS where id in :jobId", nativeQuery = true)
  List<OthersBusiness> findAllById(Pageable pageable, @Param("jobId") Long[] jobId);
}
