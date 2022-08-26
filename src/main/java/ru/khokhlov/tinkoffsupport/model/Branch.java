package ru.khokhlov.tinkoffsupport.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Branch {

    private int branchId;

    @NotEmpty(message = "Answer shouldn't be empty")
    @Size(min = 1, max = 30, message = "Answer should be between 1 and 30 characters")
    private String answer;
    private int nodeId;
    private int nextNodeId;

    public Branch(){}

    public Branch(int branchId, String answer, int nodeId, int nextNodeId) {
        this.branchId = branchId;
        this.answer = answer;
        this.nodeId = nodeId;
        this.nextNodeId = nextNodeId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(int nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    @Override
    public String toString(){
        return branchId + " " + answer + " " + nodeId + " " + nextNodeId;
    }
}
