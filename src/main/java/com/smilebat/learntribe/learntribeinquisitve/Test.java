package com.smilebat.learntribe.learntribeinquisitve;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/mock")
public class Test {
    @GetMapping
    public List<String> getUserList() {
        return Arrays.asList("mock");
    }
}
