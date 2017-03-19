package net.oschina.app.v2.db;

import net.oschina.app.v2.AppContext;

import org.aisen.orm.SqliteUtility;
import org.aisen.orm.SqliteUtilityBuilder;
import org.aisen.orm.extra.Extra;

import java.util.List;

/**
 * 作者：伍岳 on 2016/11/22 15:24
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
public class DBHelper {

    private static <T> Extra getExtra(Class<T> cls) {
        String userId = "0";
        Extra extra = new Extra(userId, cls.getSimpleName());
        return extra;
    }

    private static SqliteUtility getDBSqlite() {
        final String db_name = "assistant_db";
        if (SqliteUtility.getInstance(db_name) == null)
            new SqliteUtilityBuilder().configDBName(db_name).configVersion(1).build(AppContext.instance());

        return SqliteUtility.getInstance(db_name);
    }

    public static <T> List<T> findData(Class<T> cls) {
        Extra extra = getExtra(cls);
        List<T> beanList = getDBSqlite().select(extra, cls);

        return beanList;
    }

    public static DraftBean findDrafBean(int askId){
        DraftBean data=null;
        List<DraftBean> beanList=findData(DraftBean.class);
        if(beanList!=null&&beanList.size()>0){
            for(DraftBean item:beanList){
                if(askId==item.getQuestionId()){
                    data=item;
                    break;
                }
            }
        }
        return data;
    }

    public static DraftBean findDrafBean(int type,int askId,int commentId){
        DraftBean data=null;
        Extra extra = getExtra(DraftBean.class);

        String selection="type=? and askId=? and commentId=?";
        String[] selectionArgs=new String[]{String.valueOf(type),String.valueOf(askId),String.valueOf(commentId)};

        List<DraftBean> beanList = getDBSqlite().select(DraftBean.class,selection,selectionArgs);

        if(beanList!=null&&beanList.size()>0){
            data=beanList.get(0);
        }
        return data;
    }

    public static <T> void saveData(T data) {
        Extra extra = getExtra(data.getClass());
        getDBSqlite().insert(extra, data);
    }

    public static <T> void clearData(Class<T> cls) {
        getDBSqlite().deleteAll(null, cls);
    }

    public static void clearAllData() {
        getDBSqlite().deleteAll();
    }

    public static <T extends BaseDataBean> void deleteData(T data) {
        if (data == null) {
            return;
        }
        Extra extra = getExtra(data.getClass());
        getDBSqlite().deleteById(extra, data.getClass(), data.getId());
    }
}
