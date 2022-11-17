package com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
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

  public static final String USER_DETAILS_NAME = "userDetails";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  private String keyCloakId;

  private String name;
  private String email;

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

  @OneToMany(mappedBy = USER_DETAILS_NAME)
  @JsonIgnoreProperties(
      value = {USER_DETAILS_NAME},
      allowSetters = true)
  private Set<WorkExperience> workExperiences = new HashSet<>();
}
