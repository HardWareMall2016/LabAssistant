package net.oschina.app.v2.model.event;

/**
 * Created by WuYue on 2016/5/25.
 */
public class ToggleFilterbarEvent {
    public boolean showFilterBar;

    public ToggleFilterbarEvent(boolean show){
        showFilterBar=show;
    }
}
