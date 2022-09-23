package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smilebat.learntribe.inquisitve.AssessmentType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

  private String name;

  @Enumerated(EnumType.STRING)
  private AssessmentType type;

  private float progress;
  private long questions;
  private String status;
  private String difficulty;
  private String description;
  private String title;
  private String subTitle;
  private String createdBy;

  @OneToMany(mappedBy = ASSESSMENT_NAME)
  @JsonIgnoreProperties(
      value = {ASSESSMENT_NAME},
      allowSetters = true)
  private Set<Challenge> challenges = new HashSet<>();

  /**
   * Mock getter method.
   *
   * @return List of {@link Assessment}
   */
  public static List<Assessment> generateMockAssessments() {
    Assessment assessment1 = new Assessment();
    assessment1.setId(1L);
    assessment1.setName("SQL");
    assessment1.setProgress(33.33f);
    assessment1.setQuestions(40);
    assessment1.setType(AssessmentType.OBJECTIVE);
    assessment1.setDescription("Real Time description");
    assessment1.setSubTitle("THis is the subtitle");
    assessment1.setDifficulty("Easy");
    assessment1.setStatus("Pending");

    Assessment assessment2 = new Assessment();
    assessment2.setId(3L);
    assessment2.setName("NETWORKING");
    assessment2.setProgress(0f);
    assessment2.setQuestions(25);
    assessment2.setType(AssessmentType.OBJECTIVE);
    assessment2.setDescription("Real Time description");
    assessment2.setSubTitle("THis is the subtitle");
    assessment2.setDifficulty("Difficult");
    assessment2.setStatus("Completed");

    List<Assessment> assessments = new ArrayList<>(2);

    assessments.add(assessment1);
    assessments.add(assessment2);

    return assessments;
  }
}
