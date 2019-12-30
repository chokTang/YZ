//package com.sxtx.user.dbdata.manger;
//
//import android.content.Context;
//
//
//import com.sxtx.user.model.account.AccountModelDao;
//import com.sxtx.user.model.account.DaoMaster;
//import com.sxtx.user.model.account.DaoSession;
//
//import org.greenrobot.greendao.database.Database;
//
///**
// * Created by longshao on 2017/8/1.
// */
//
//public class DataBaseManager {
//
//    private DaoSession mDaoSession = null;
//    private AccountModelDao modelDao = null;
//
//    private DataBaseManager() {
//    }
//
//    private static final class Holder {
//        private static final DataBaseManager INSTANCE = new DataBaseManager();
//    }
//
//    private void initDao(Context context) {
//        final DataOpenHelper helper = new DataOpenHelper(context, "long_ec.db");
//        final Database database = helper.getWritableDb();
//        mDaoSession = new DaoMaster(database).newSession();
//        modelDao = mDaoSession.getAccountModelDao();
//    }
//
//    public static DataBaseManager getIntance() {
//        return Holder.INSTANCE;
//    }
//
//    public DataBaseManager init(Context context) {
//        initDao(context);
//        return this;
//    }
//
//    /**
//     * 获取到某个数据库的中实体
//     * @return
//     */
//    public final AccountModelDao getAccountDao() {
//        return modelDao;
//    }
//}
