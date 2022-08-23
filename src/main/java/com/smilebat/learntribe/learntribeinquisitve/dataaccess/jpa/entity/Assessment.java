package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.smilebat.learntribe.inquisitve.AssessmentType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Assessment Entity representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Getter
@Setter
@Table(name = "ASSESSMENT")
@Entity
@NamedNativeQuery(
    name = "Assessment.findByUserId",
    query = "SELECT * FROM assessment WHERE user_details_id = ?",
    resultClass = UserProfile.class)
@SuppressFBWarnings(justification = "Generated code")
public class Assessment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;
  private AssessmentType type;
  private float progress;
  private long questions;

  @ManyToOne(optional = false)
  @NotNull
  private UserProfile userDetails;

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

    Assessment assessment2 = new Assessment();
    assessment2.setId(3L);
    assessment2.setName("NETWORKING");
    assessment2.setProgress(0f);
    assessment2.setQuestions(25);
    assessment2.setType(AssessmentType.OBJECTIVE);

    List<Assessment> assessments = new ArrayList<>(2);

    assessments.add(assessment1);
    assessments.add(assessment2);

    return assessments;
  }
}
