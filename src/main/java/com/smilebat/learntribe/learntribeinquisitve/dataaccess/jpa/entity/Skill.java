package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Skill Entity Representation of DB
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Getter
@Setter
@Table(name = "SKILL")
@Entity
@SuppressFBWarnings(justification = "Generated code")
public class Skill {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String skillName;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"skills", "workExperiences"},
      allowSetters = true)
  private UserProfile userDetails;

  @Override
  public int hashCode() {
    return Objects.hash(skillName,userDetails);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Skill other = (Skill) obj;
    return Objects.equals(skillName, other.skillName) && Objects.equals(userDetails, other.userDetails);
  }
}
