package com.coderun.compiler.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationResult {
    private boolean success;
    private String output;
    private String error;
}
