package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
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
@Getter
@Setter
@Table(name = "OTHERS_BUSINESS")
@Entity
@NamedNativeQuery(
    name = "Assessment.findByUserId",
    query = "SELECT * FROM assessment WHERE user_details_id = ?",
    resultClass = UserProfile.class)
@SuppressFBWarnings(justification = "Generated code")
public class OthersBusiness {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String title;
  private String description;

  @ElementCollection(targetClass = String.class)
  private List<String> rolesAndResponsibilities;

  @ElementCollection(targetClass = String.class)
  private List<String> requiredSkills;

  private String createdBy;
}
