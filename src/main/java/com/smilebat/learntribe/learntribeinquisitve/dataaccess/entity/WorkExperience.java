package com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/** WorkExperience table in Database */
@Getter
@Setter
@Table(name = "WORK_EXPERIENCE")
@Entity
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
      value = {"skills", "workExperiences"},
      allowSetters = true)
  private UserDetails userDetails;
}
