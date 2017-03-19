package net.oschina.app.v2.db;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;

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
    //草稿类型：1：普通，2：互动
    private int draftType=1;

    private long saveTime=System.currentTimeMillis();
    private String questionTitle;
    private int questionId;
    private String draftContent;
    private String headerUnDelete;
    private String toSomeone;
    private String superlist = "";
    private HashMap<Integer, UserBean> userList;

    //互动列表中的草稿
    private int type;

    //commnet
    private int commentId;
    private int commentAid;
    private int commentQid;
    private int commentAuid;
    private String commentNickName;

    //Ask
    private int askId;
    private int askUid;
    private String askNickname;

    private String askHead;
    private String askContent;
    private String askIntPutTime;
    private String askImage;
    private String askNewContent;


    public int getDraftType() {
        return draftType;
    }

    public void setDraftType(int draftType) {
        this.draftType = draftType;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAskId() {
        return askId;
    }

    public void setAskId(int askId) {
        this.askId = askId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommentAid() {
        return commentAid;
    }

    public void setCommentAid(int commentAid) {
        this.commentAid = commentAid;
    }

    public int getCommentQid() {
        return commentQid;
    }

    public void setCommentQid(int commentQid) {
        this.commentQid = commentQid;
    }

    public int getCommentAuid() {
        return commentAuid;
    }

    public void setCommentAuid(int commentAuid) {
        this.commentAuid = commentAuid;
    }

    public String getCommentNickName() {
        return commentNickName;
    }

    public void setCommentNickName(String commentNickName) {
        this.commentNickName = commentNickName;
    }

    public int getAskUid() {
        return askUid;
    }

    public void setAskUid(int askUid) {
        this.askUid = askUid;
    }

    public String getAskNickname() {
        return askNickname;
    }

    public void setAskNickname(String askNickname) {
        this.askNickname = askNickname;
    }

    public String getAskHead() {
        return askHead;
    }

    public void setAskHead(String askHead) {
        this.askHead = askHead;
    }

    public String getAskContent() {
        return askContent;
    }

    public void setAskContent(String askContent) {
        this.askContent = askContent;
    }

    public String getAskIntPutTime() {
        return askIntPutTime;
    }

    public void setAskIntPutTime(String askIntPutTime) {
        this.askIntPutTime = askIntPutTime;
    }

    public String getAskImage() {
        return askImage;
    }

    public void setAskImage(String askImage) {
        this.askImage = askImage;
    }

    public String getAskNewContent() {
        return askNewContent;
    }

    public void setAskNewContent(String askNewContent) {
        this.askNewContent = askNewContent;
    }
}
