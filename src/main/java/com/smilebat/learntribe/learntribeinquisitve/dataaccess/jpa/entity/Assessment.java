package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smilebat.learntribe.enums.AssessmentDifficulty;
import com.smilebat.learntribe.enums.AssessmentType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Assessment Entity representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Table(name = "ASSESSMENT")
@Entity
@Getter
@Setter
@SuppressFBWarnings(justification = "Generated code")
public class Assessment {
  public static final String ASSESSMENT_NAME = "assessmentInfo";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String subTitle;

  @Enumerated(EnumType.STRING)
  private AssessmentType type;

  private float progress;
  private long questions;
  private String status;

  @Enumerated(EnumType.STRING)
  private AssessmentDifficulty difficulty;

  private String description;

  private String title;

  private String createdBy;

  private Long relatedJobId;

  @OneToMany(mappedBy = ASSESSMENT_NAME, fetch = FetchType.LAZY)
  @JsonIgnoreProperties(
      value = {ASSESSMENT_NAME},
      allowSetters = true)
  private Set<Challenge> challenges;
}
