package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrandList extends Entity implements ListEntity {
	private static final long serialVersionUID = 1097118838408833362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	private List<Brand> brandlist = new ArrayList<Brand>();

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

	public List<Brand> getBrandlist() {
		return brandlist;
	}

	public void setBrandlist(List<Brand> brandlist) {
		this.brandlist = brandlist;
	}

	@Override
	public List<?> getList() {
		// TODO Auto-generated method stub
		return brandlist;
	}

	public static BrandList parse(String jsonStr) throws IOException,
			AppException {
		BrandList brandlist = new BrandList();
		Brand brand = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			brandlist.setCode(json.getInt("code"));
			brandlist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			for (int i = 0; i < dataList.length(); i++) {
				brand = brand.parse(dataList.getJSONObject(i));
				brandlist.getBrandlist().add(brand);
			}
			
			//Collections.sort(brandlist.getBrandlist());
		} catch (JSONException e) {

		}

		return brandlist;

	}
}
