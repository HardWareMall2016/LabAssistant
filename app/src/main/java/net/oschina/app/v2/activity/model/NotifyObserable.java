package net.oschina.app.v2.activity.model;

import java.util.Observable;

public class NotifyObserable extends Observable {

	public static final String NOTICE_NEW_MESSAGE = "new_message";
	
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	public static class NotifyData {
		String key;
		Object value;

		public NotifyData(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}
}
