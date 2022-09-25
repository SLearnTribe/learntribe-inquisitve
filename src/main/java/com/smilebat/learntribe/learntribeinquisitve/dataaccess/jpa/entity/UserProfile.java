package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smilebat.learntribe.inquisitve.UserRole;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * User Profile representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Table(name = "USER_PROFILE")
@Entity
@SuppressFBWarnings(justification = "Generated code")
@Getter
@Setter
public class UserProfile {

  public static final String USER_DETAILS_NAME = "userDetails";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String keyCloakId;
  private String name;
  private String email;
  private String country; /*IMportant*/
  private String linkedIn;
  private String gitHub;
  
  private String skills; /*IMportant*/

  @Lob private String about; /*IMportant*/
  private Long phone;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToMany(mappedBy = USER_DETAILS_NAME)
  @JsonIgnoreProperties(
      value = {USER_DETAILS_NAME},
      allowSetters = true)
  private Set<WorkExperience> workExperiences = new HashSet<>();
}
