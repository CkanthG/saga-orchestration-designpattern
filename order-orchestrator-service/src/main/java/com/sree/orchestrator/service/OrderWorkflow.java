package com.sree.orchestrator.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderWorkflow implements Workflow{

    private final List<WorkflowStep> steps;

    @Override
    public List<WorkflowStep> getSteps() {
        return steps;
    }
}
