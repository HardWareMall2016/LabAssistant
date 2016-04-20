package net.oschina.app.v2.activity.home.fragment;

import net.oschina.app.v2.base.BaseFragment;

public class HomeBaseFragment extends BaseFragment {
	
//	protected int mCatalog = NewsList.CATALOG_ALL;
//	private int mLastId = 0;
//	
//	
//	private String getCacheKey() {
//		return new StringBuffer(getCacheKeyPrefix()).append(mCatalog)
//				.append("_").append(mLastId).append("_")
//				.append(TDevice.getPageSize()).toString();
//	}
//	
//	private String getCacheKeyPrefix(){
//		return "newslist_";
//	}
//	
//	private class CacheTask extends AsyncTask<String, Void, ListEntity> {
//		private WeakReference<Context> mContext;
//
//		private CacheTask(Context context) {
//			mContext = new WeakReference<Context>(context);
//		}
//
//		@Override
//		protected ListEntity doInBackground(String... params) {
//			Serializable seri = CacheManager.readObject(mContext.get(),
//					params[0]);
//			if (seri == null) {
//				return null;
//			} else {
//				return readList(seri);
//			}
//		}
//
//		@Override
//		protected void onPostExecute(ListEntity list) {
//			super.onPostExecute(list);
//			if (list != null) {
//				executeOnLoadDataSuccess(list.getList());
//			} else {
//				executeOnLoadDataError(null);
//			}
//			executeOnLoadFinish();
//		}
//	}
//
//	private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
//		private WeakReference<Context> mContext;
//		private Serializable seri;
//		private String key;
//
//		private SaveCacheTask(Context context, Serializable seri, String key) {
//			mContext = new WeakReference<Context>(context);
//			this.seri = seri;
//			this.key = key;
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//			CacheManager.saveObject(mContext.get(), seri, key);
//			return null;
//		}
//	}
//
//	class ParserTask extends AsyncTask<Void, Void, String> {
//
//		private byte[] reponseData;
//		private boolean parserError;
//		private List<?> list;
//
//		public ParserTask(byte[] data) {
//			this.reponseData = data;
//		}
//
//		@Override
//		protected String doInBackground(Void... params) {
//			try {
//				ListEntity data = parseList(new ByteArrayInputStream(
//						reponseData));
//				new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
//				list = data.getList();
//			} catch (Exception e) {
//				e.printStackTrace();
//				parserError = true;
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			if (parserError) {
//				readCacheData(getCacheKey());
//			} else {
//				executeOnLoadDataSuccess(list);
//				executeOnLoadFinish();
//			}
//		}
//	}
//	
//	private void readCacheData(String cacheKey) {
//		cancelReadCacheTask();
//		mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
//	}
}
