package com.coderun.compiler.service;

import com.coderun.compiler.dto.CompilationResult;
import org.springframework.stereotype.Service;

@Service
public class CompilerService {

    private final DockerService dockerService;

    public CompilerService(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    public CompilationResult compile(String javaCode) {
        try {
            if (javaCode == null || javaCode.trim().isEmpty()) {
                return new CompilationResult(false, "", "Java code can not be empty");
            }

            String output = dockerService.compileInContainer(javaCode);

            boolean success = !output.toLowerCase().contains("error")
                    || !output.toLowerCase().contains("exception")
                    || !output.toLowerCase().contains("fail");

            return new CompilationResult(success, output, "");
        } catch (Exception e) {
            return new CompilationResult(false, "", "Compilation error: " + e.getMessage());
        }
    }
}
