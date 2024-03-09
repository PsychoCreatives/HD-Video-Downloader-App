package hd.tubex.snap.vmate.itube.mixmate.interfaces;


import hd.tubex.snap.vmate.itube.mixmate.model.FBStoryModel.NodeModel;
import hd.tubex.snap.vmate.itube.mixmate.model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);
    void fbUserListClick(int position, NodeModel trayModel);
}
