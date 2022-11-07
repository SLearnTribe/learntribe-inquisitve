package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.smilebat.learntribe.inquisitve.JobStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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
@SuppressFBWarnings(justification = "Generated code")
public class OthersBusiness {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Lob private String title;
  @Lob private String description;

  @Lob private String rolesAndResponsibilities;
  @Lob private String requiredSkills;

  private String createdBy;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Enumerated(EnumType.STRING)
  private JobStatus status;
}
