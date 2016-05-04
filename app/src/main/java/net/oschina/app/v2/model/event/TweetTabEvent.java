package net.oschina.app.v2.model.event;

/**
 * Created by WuYue on 2016/5/4.
 */
public class TweetTabEvent {
    public int tabIndex=0;
    public boolean byUser=true;
    public TweetTabEvent(int tabIndex){
        this.tabIndex=tabIndex;
    }

    public TweetTabEvent(int tabIndex,boolean byUser){
        this.tabIndex=tabIndex;
        this.byUser=byUser;
    }
}
