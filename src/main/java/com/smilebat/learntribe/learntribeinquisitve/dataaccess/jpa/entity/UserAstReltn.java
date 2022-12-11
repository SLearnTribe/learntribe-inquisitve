package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.smilebat.learntribe.inquisitve.AssessmentStatus;
import com.smilebat.learntribe.inquisitve.UserAstReltnType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

/**
 * Defines the relationship between User and Assessment Entity in DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Getter
@Setter
@Table(name = "USR_AST_RELTN")
@Entity
@Indexed
@SuppressFBWarnings(justification = "Generated code")
public class UserAstReltn {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Field(termVector = TermVector.YES)
  private String userId;

  private Long assessmentId;

  @Field(termVector = TermVector.YES)
  private String assessmentTitle;

  @Field(termVector = TermVector.YES)
  @Enumerated(EnumType.STRING)
  private AssessmentStatus status;

  @Enumerated(EnumType.STRING)
  private UserAstReltnType userAstReltnType;
}
