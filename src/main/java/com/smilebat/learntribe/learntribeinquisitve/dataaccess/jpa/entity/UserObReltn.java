package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.smilebat.learntribe.enums.HiringStatus;
import com.smilebat.learntribe.enums.UserObReltnType;
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

/**
 * Defines the relationship between User and Job Entity in DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Getter
@Setter
@Table(name = "USR_OB_RELTN")
@Entity
@SuppressFBWarnings(justification = "Generated code")
public class UserObReltn {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String userId;

  @Enumerated(EnumType.STRING)
  private HiringStatus hiringStatus;

  @Enumerated(EnumType.STRING)
  private UserObReltnType userObReltn;

  private Long jobId;
}
