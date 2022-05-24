package com.smilebat.learntribe.inquisitve.controllers;

import com.smilebat.learntribe.inquisitve.helpers.ResponseBuilder;
import com.smilebat.learntribe.inquisitve.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserManagementController {

  private final UserManagementService service;
  private final ResponseBuilder responseBuilder;
  private final HttpServletRequest request;
  @GetMapping
  public ResponseEntity<List<String>> getUserList() {

    return ResponseEntity.ok(Arrays.asList("test success"));
  }

}
