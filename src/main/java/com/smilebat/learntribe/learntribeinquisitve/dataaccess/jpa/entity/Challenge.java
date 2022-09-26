package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
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

/**
 * Represents a single Objective Question for user.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Table(name = "CHALLENGE")
@Entity
@Getter
@Setter
@SuppressFBWarnings(justification = "Generated code")
public class Challenge {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String question;
  private String[] options;
  private String answer;

  //  private ChallengeCategory

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"challenges"},
      allowSetters = true)
  private Assessment assessmentInfo;

  @Override
  public int hashCode() {
    return Objects.hash(question, answer);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Challenge other = (Challenge) obj;
    return Objects.equals(this.question, other.question)
        && Objects.equals(this.answer, other.answer);
  }
}
