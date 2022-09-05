package com.smilebat.learntribe.learntribeinquisitve.services;

import com.smilebat.learntribe.inquisitve.response.UserProfileResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.SkillRepository;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Skill;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {
  private final SkillRepository skillRepository;
  private final UserInfoService userInfoService;

  public List getUserInfo(String skillName) {
    if (skillName == null || skillName.isEmpty()) {
      return Collections.emptyList();
    }
    List<Skill> users = skillRepository.findAllBySkillName(skillName);
    List<UserProfileResponse> userProfileResponses = new ArrayList<UserProfileResponse>();
    for (Skill user : users) {
      log.info(user.getUserDetails().getKeyCloakId());
      userProfileResponses.add(userInfoService.getUserInfo(user.getUserDetails().getKeyCloakId()));
    }
    return userProfileResponses;
  }
}
