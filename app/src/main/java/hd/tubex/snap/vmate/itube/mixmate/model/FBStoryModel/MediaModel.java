package hd.tubex.snap.vmate.itube.mixmate.model.FBStoryModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MediaModel implements Serializable {
    @SerializedName("media")
    private MediaDataModel mediaDataModel;

    public MediaDataModel getMediaDataModel() {
        return mediaDataModel;
    }

    public void setMediaDataModel(MediaDataModel mediaDataModel) {
        this.mediaDataModel = mediaDataModel;
    }
}
