package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Work Experience representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Getter
@Setter
@Table(name = "WORK_EXPERIENCE")
@Entity
@SuppressFBWarnings(justification = "Generated code")
public class WorkExperience implements Experience, Comparable<WorkExperience> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String location;

  private String orgName;
  private String designation;
  private Integer years;
  protected Instant startDate;
  protected Instant endDate;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"educationExperiences", "workExperiences"},
      allowSetters = true)
  protected UserProfile userProfile;

  @Override
  public int compareTo(WorkExperience o) {
    if (o != null && endDate != null) {

      return o.endDate.compareTo(endDate);
    }
    return 0;
  }

  /** Wrapper class for comparator sorting based on date. */
  public static class Comparators {
    public static final Comparator<WorkExperience> END_DATE =
        Comparator.comparing(WorkExperience::getEndDate);
  }
}
