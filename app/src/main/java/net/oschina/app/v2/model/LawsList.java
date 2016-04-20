package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 法律文献列表实体类
 * 
 * @author jaysi
 * 
 */
public class LawsList extends Entity implements ListEntity {
	private static final long serialVersionUID = 1067118838408833369L;

	private int code;
	private String desc;
	private List<Laws> lawsList = new ArrayList<Laws>();

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<Laws> getLawsList() {
		return lawsList;
	}

	public void setLawsList(List<Laws> lawsList) {
		this.lawsList = lawsList;
	}

	@Override
	public List<?> getList() {
		// TODO Auto-generated method stub
		return lawsList;
	}

	public static LawsList parseList(String jsonStr) throws IOException,
			AppException {
		LawsList lawsList = new LawsList();
		Laws laws = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			lawsList.setCode(json.getInt("code"));
			lawsList.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			for (int i = 0; i < dataList.length(); i++) {
				laws = laws.parse(dataList.getJSONObject(i));
				lawsList.getLawsList().add(laws);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lawsList;
	}
}
