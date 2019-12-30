//package com.sxtx.user.dbdata.manger;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.github.yuweiguocn.library.greendao.MigrationHelper;
//import com.sxtx.user.model.account.AccountModelDao;
//import com.sxtx.user.model.account.DaoMaster;
//
//import org.greenrobot.greendao.database.Database;
//
///**
// * Created by longshao on 2017/8/1.
// */
//
//public class DataOpenHelper extends DaoMaster.OpenHelper {
//
//    public DataOpenHelper(Context context, String name) {
//        super(context, name);
//    }
//
//    public DataOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
//        super(context, name, factory);
//    }
//
//    @Override
//    public void onCreate(Database db) {
//        super.onCreate(db);
//    }
//
//    @Override
//    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        MigrationHelper.migrate(db, AccountModelDao.class);//添加数据库更新
//    }
//
//}
