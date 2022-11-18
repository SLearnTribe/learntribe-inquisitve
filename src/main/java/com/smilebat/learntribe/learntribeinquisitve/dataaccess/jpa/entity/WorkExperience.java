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
public class WorkExperience {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String orgName;
  private String designation;
  private String startDate;
  private String endDate;
  private Integer years;
  private String location;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"educationExperiences", "workExperiences"},
      allowSetters = true)
  private UserProfile userProfile;

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate, designation, userProfile);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    WorkExperience other = (WorkExperience) obj;
    return Objects.equals(designation, other.designation)
        && Objects.equals(startDate, other.startDate)
        && Objects.equals(endDate, other.endDate)
        && Objects.equals(userProfile, other.userProfile);
  }
}
