package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smilebat.learntribe.enums.Gender;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
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
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TermVector;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

/**
 * User Profile representation of DB.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Sanjay
 */
@Table(name = "USER_PROFILE")
@Entity
@Indexed
@SuppressFBWarnings(justification = "Generated code")
@Getter
@Setter
@AnalyzerDef(
    name = "textanalyzer",
    tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
    filters = {
      @TokenFilterDef(factory = LowerCaseFilterFactory.class),
      @TokenFilterDef(
          factory = SnowballPorterFilterFactory.class,
          params = {@Parameter(name = "language", value = "English")})
    })
public class UserProfile {

  public static final String USER_DETAILS_NAME = "userProfile";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String keyCloakId;

  private String name;
  private String email;
  private String currentDesignation;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Field(termVector = TermVector.YES)
  private String country;

  private String linkedIn;
  private String gitHub;

  @Field(
      termVector = TermVector.YES,
      store = Store.NO,
      analyzer = @Analyzer(definition = "textanalyzer"))
  private String skills;

  @Field(termVector = TermVector.YES)
  @Lob
  private String about;

  private Long phone;

  @OneToMany(mappedBy = USER_DETAILS_NAME, cascade = CascadeType.ALL)
  @JsonIgnoreProperties(
      value = {USER_DETAILS_NAME},
      allowSetters = true)
  private Set<WorkExperience> workExperiences = new TreeSet<>(WorkExperience.Comparators.END_DATE);

  @OneToMany(mappedBy = USER_DETAILS_NAME, cascade = CascadeType.ALL)
  @JsonIgnoreProperties(
      value = {USER_DETAILS_NAME},
      allowSetters = true)
  private Set<EducationExperience> educationExperiences =
      new TreeSet<>(EducationExperience.Comparators.END_DATE);

  /**
   * Assigns the work experiences for save.
   *
   * @param experiences the Set of {@link WorkExperience}.
   */
  public void setWorkExperiences(Set<WorkExperience> experiences) {
    if (experiences != null) {
      experiences.forEach(experience -> experience.setUserProfile(this));
      workExperiences = experiences;
    }
  }

  /**
   * Assigns the education experiences for save.
   *
   * @param experiences the Set of {@link EducationExperience}.
   */
  public void setEducationExperiences(Set<EducationExperience> experiences) {
    if (experiences != null) {
      experiences.forEach(experience -> experience.setUserProfile(this));
      educationExperiences = experiences;
    }
  }
}
