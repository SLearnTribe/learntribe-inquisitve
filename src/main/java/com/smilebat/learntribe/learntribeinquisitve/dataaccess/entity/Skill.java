package com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Representing Skills Data In DB */
@Getter
@Setter
@Table(name = "SKILL")
@Entity
public class Skill {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  String skillName;

  @ManyToOne(optional = false)
  @NotNull
  @JsonIgnoreProperties(
      value = {"skills", "workExperiences"},
      allowSetters = true)
  private UserDetails userDetails;
}
