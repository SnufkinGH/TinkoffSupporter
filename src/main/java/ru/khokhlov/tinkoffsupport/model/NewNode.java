package ru.khokhlov.tinkoffsupport.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class NewNode {

    private int previousNodeId;

    @NotEmpty(message = "Answer shouldn't be empty")
    @Size(min = 1, max = 30, message = "Answer should be between 1 and 30 characters")
    private String edgeMessage;

    @NotEmpty(message = "Message shouldn't be empty")
    @Size(min = 4, max = 50, message = "Message should be between 4 and 50 characters")
    private String nodeMessage;

    public NewNode(){}

    public NewNode(int previousNodeId, String edgeMessage, String nodeMessage) {
        this.previousNodeId = previousNodeId;
        this.edgeMessage = edgeMessage;
        this.nodeMessage = nodeMessage;
    }

    public int getPreviousNodeId() {
        return previousNodeId;
    }

    public void setPreviousNodeId(int previousNodeId) {
        this.previousNodeId = previousNodeId;
    }

    public String getEdgeMessage() {
        return edgeMessage;
    }

    public void setEdgeMessage(String edgeMessage) {
        this.edgeMessage = edgeMessage;
    }

    public String getNodeMessage() {
        return nodeMessage;
    }

    public void setNodeMessage(String nodeMessage) {
        this.nodeMessage = nodeMessage;
    }
}
