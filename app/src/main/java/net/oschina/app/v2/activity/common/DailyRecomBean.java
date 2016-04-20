package net.oschina.app.v2.activity.common;

public class DailyRecomBean {
 private int id;
 private String title;
 private String time;
 private String imageUrl;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
@Override
public String toString() {
	return "DailyRecomBean [id=" + id + ", title=" + title + ", time=" + time
			+ ", imageUrl=" + imageUrl + "]";
}
public DailyRecomBean(int id, String title, String time, String imageUrl) {
	super();
	this.id = id;
	this.title = title;
	this.time = time;
	this.imageUrl = imageUrl;
}
public DailyRecomBean() {
	super();
}
 
 
}
