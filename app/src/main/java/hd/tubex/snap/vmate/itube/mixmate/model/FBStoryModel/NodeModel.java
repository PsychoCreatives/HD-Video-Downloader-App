package hd.tubex.snap.vmate.itube.mixmate.model.FBStoryModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NodeModel implements Serializable {
    @SerializedName("node")
    private NodeDataModel nodeDataModel;

    public NodeDataModel getNodeDataModel() {
        return nodeDataModel;
    }

    public void setNodeDataModel(NodeDataModel nodeDataModel) {
        this.nodeDataModel = nodeDataModel;
    }

}
