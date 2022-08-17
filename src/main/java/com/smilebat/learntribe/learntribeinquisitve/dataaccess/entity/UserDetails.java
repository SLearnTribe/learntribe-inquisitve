package com.smilebat.learntribe.learntribeinquisitve.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Data;

/** Database Equivalent Table of User Profile */
@Data
@Table(name = "USER_PROFILE")
@Entity
public class UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private Long keycloakId;
  private String name;
  private String email;

  private String country;
  private String linkedIn;
  private String gitHub;
  private String about;
  private String phone;

  @OneToMany(mappedBy = "userDetails")
  @JsonIgnoreProperties(
      value = {"userDetails"},
      allowSetters = true)
  private Set<Skill> skills = new HashSet<>();

  /** A relationship */
  @OneToMany(mappedBy = "userDetails")
  @JsonIgnoreProperties(
      value = {"userDetails"},
      allowSetters = true)
  private Set<WorkExperience> workExperiences = new HashSet<>();
}
