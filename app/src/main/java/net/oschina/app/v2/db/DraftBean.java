package net.oschina.app.v2.db;

import net.oschina.app.v2.activity.tweet.model.UserBean;

import java.util.HashMap;

/**
 * 作者：伍岳 on 2016/11/22 15:59
 * 邮箱：wuyue8512@163.com
 * //
 * //         .............................................
 * //                  美女坐镇                  BUG辟易
 * //         .............................................
 * //
 * //                       .::::.
 * //                     .::::::::.
 * //                    :::::::::::
 * //                 ..:::::::::::'
 * //              '::::::::::::'
 * //                .::::::::::
 * //           '::::::::::::::..
 * //                ..::::::::::::.
 * //              ``::::::::::::::::
 * //               ::::``:::::::::'        .:::.
 * //              ::::'   ':::::'       .::::::::.
 * //            .::::'      ::::     .:::::::'::::.
 * //           .:::'       :::::  .:::::::::' ':::::.
 * //          .::'        :::::.:::::::::'      ':::::.
 * //         .::'         ::::::::::::::'         ``::::.
 * //     ...:::           ::::::::::::'              ``::.
 * //    ```` ':.          ':::::::::'                  ::::..
 * //                       '.:::::'                    ':'````..
 * //
 */
public class DraftBean extends BaseDataBean{
    private long saveTime=System.currentTimeMillis();
    private String questionTitle;
    private int questionId;
    private String draftContent;
    private String headerUnDelete;
    private String toSomeone;
    private String superlist = "";
    private HashMap<Integer, UserBean> userList;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public String getDraftContent() {
        return draftContent;
    }

    public void setDraftContent(String draftContent) {
        this.draftContent = draftContent;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getHeaderUnDelete() {
        return headerUnDelete;
    }

    public void setHeaderUnDelete(String headerUnDelete) {
        this.headerUnDelete = headerUnDelete;
    }

    public String getToSomeone() {
        return toSomeone;
    }

    public void setToSomeone(String toSomeone) {
        this.toSomeone = toSomeone;
    }

    public String getSuperlist() {
        return superlist;
    }

    public void setSuperlist(String superlist) {
        this.superlist = superlist;
    }

    public HashMap<Integer, UserBean> getUserList() {
        return userList;
    }

    public void setUserList(HashMap<Integer, UserBean> userList) {
        this.userList = userList;
    }
}
