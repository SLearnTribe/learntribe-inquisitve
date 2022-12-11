package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
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
public class EducationExperience {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String location;
  private String collegeName;

  private String degree;

  private String dateOfCompletion;

  private String fieldOfStudy;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"educationExperiences", "workExperiences"},
      allowSetters = true)
  protected UserProfile userProfile;

  @Override
  public int hashCode() {
    return Objects.hash(collegeName, dateOfCompletion, degree, userProfile);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    EducationExperience other = (EducationExperience) obj;
    return Objects.equals(collegeName, other.collegeName)
        && Objects.equals(dateOfCompletion, other.dateOfCompletion)
        && Objects.equals(degree, other.degree)
        && Objects.equals(userProfile, other.userProfile);
  }
}
