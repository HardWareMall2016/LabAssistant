package net.oschina.app.v2.activity.user.model;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;
import net.oschina.app.v2.model.ListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZhiChiWoDeList extends Entity implements ListEntity {
    private int code;
    private String desc;
    private String supportednum;
    private String todaysupportednum;


    private List<ZhiChiWoDe> xitongxiaoxilist = new ArrayList<ZhiChiWoDe>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<ZhiChiWoDe> getXitongxiaoxilist() {
        return xitongxiaoxilist;
    }


    public String getSupportednum() {
        return supportednum;
    }

    public void setSupportednum(String supportednum) {
        this.supportednum = supportednum;
    }

    public String getTodaysupportednum() {
        return todaysupportednum;
    }

    public void setTodaysupportednum(String todaysupportednum) {
        this.todaysupportednum = todaysupportednum;
    }

    public static ZhiChiWoDeList parse(String jsonStr) throws IOException,
            AppException {
        ZhiChiWoDeList xitongxiaoxilist = new ZhiChiWoDeList();
        ZhiChiWoDe xiTongXiaoXi = null;
        try {
            JSONObject json = new JSONObject(jsonStr);

            json= new JSONObject(json.getString("data"));
            xitongxiaoxilist.supportednum = json.optString("supportednum");
            xitongxiaoxilist.todaysupportednum = json.optString("todaysupportednum");
            JSONArray dataList = new JSONArray(json.getString("list"));
            for (int i = 0; i < dataList.length(); i++) {
                xiTongXiaoXi = ZhiChiWoDe.parse(dataList.getJSONObject(i));
                xitongxiaoxilist.getXitongxiaoxilist().add(xiTongXiaoXi);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xitongxiaoxilist;
    }

    @Override
    public List<?> getList() {
        return xitongxiaoxilist;
    }
}
