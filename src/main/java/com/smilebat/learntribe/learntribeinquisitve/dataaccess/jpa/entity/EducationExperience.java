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
 * Educational Experience representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Getter
@Setter
@Table(name = "EDUCATION_EXPERIENCE")
@Entity
@SuppressFBWarnings(justification = "Generated code")
public class EducationExperience implements Experience, Comparable<EducationExperience> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String location;
  private String collegeName;

  private String degree;

  private Instant dateOfCompletion;

  private String fieldOfStudy;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"educationExperiences", "workExperiences"},
      allowSetters = true)
  protected UserProfile userProfile;

  @Override
  public int compareTo(EducationExperience o) {
    if (o != null && dateOfCompletion != null) {
      return o.dateOfCompletion.compareTo(dateOfCompletion);
    }
    return 0;
  }

  /** Wrapper class for comparator sorting based on date. */
  public static class Comparators {
    public static final Comparator<EducationExperience> END_DATE =
        Comparator.comparing(EducationExperience::getDateOfCompletion);
  }
}
