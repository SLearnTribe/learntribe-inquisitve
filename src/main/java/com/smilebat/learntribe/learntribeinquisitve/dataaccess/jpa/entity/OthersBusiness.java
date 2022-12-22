package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.smilebat.learntribe.enums.EmploymentType;
import com.smilebat.learntribe.enums.JobStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Assessment Entity representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Getter
@Setter
@Table(name = "OTHERS_BUSINESS")
@Entity
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(justification = "Generated code")
public class OthersBusiness {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Lob private String title;
  @Lob private String description;

  @Lob private String rolesAndResponsibilities;

  @Lob private String qualificationRequired;

  @Lob private String requiredSkills;
  private Long experienceRequired;

  private String createdBy;

  private String businessName;

  private String location;

  @CreatedDate private Instant createdDate;

  @Enumerated(EnumType.STRING)
  private JobStatus status;

  @Enumerated(EnumType.STRING)
  private EmploymentType employmentType;
}
