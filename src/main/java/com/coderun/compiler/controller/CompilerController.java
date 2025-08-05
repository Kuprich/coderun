package com.coderun.compiler.controller;

import com.coderun.compiler.dto.CompilationResult;
import com.coderun.compiler.service.CompilerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CompilerController {
    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    public ResponseEntity<CompilationResult> compileJavaFile(@RequestParam("file") MultipartFile file){
        try{

        }catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body(new CompilationResult(false, "", "Internal server error:" + e.getMessage() ));

        }
    }
}
