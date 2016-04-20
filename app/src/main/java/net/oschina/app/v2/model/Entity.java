package net.oschina.app.v2.model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public abstract class Entity extends Base {

	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	protected String cacheKey;

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	 public String inStream2String(InputStream is) throws Exception {  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        byte[] buf = new byte[1024];  
	        int len = -1;  
	        while ((len = is.read(buf)) != -1) {  
	            baos.write(buf, 0, len);  
	        }  
	        return new String(baos.toByteArray());  
	    }  
}
