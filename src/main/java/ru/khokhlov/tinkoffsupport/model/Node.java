package ru.khokhlov.tinkoffsupport.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class Node {

    private int nodeId;

    @NotEmpty(message = "Message shouldn't be empty")
    @Size(min = 4, max = 50, message = "Message should be between 4 and 50 characters")
    private String message;

    private List<Branch> branches;

    public Node(){
    }

    public Node(int nodeId, String message, List<Branch> branches) {
        this.nodeId = nodeId;
        this.message = message;
        this.branches = branches;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public boolean hasBranches(){
        return branches.get(0).getAnswer() != null;
    }
}
