package com.coderun.compiler.controller;

import com.coderun.compiler.dto.CompilationResult;
import com.coderun.compiler.service.CompilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompilerController {
    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    public CompilationResult compileJavaCode(@RequestBody String javaCode) {
        return compilerService.compile(javaCode);
    }
}
