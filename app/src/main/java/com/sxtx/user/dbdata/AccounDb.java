//package com.sxtx.user.dbdata;
//
//import com.sxtx.user.dbdata.manger.DataBaseManager;
//import com.sxtx.user.model.account.AccountModel;
//
///**
// * Created by longshao on 2017/8/2.
// */
//
//public class AccounDb {
//
//    private AccounDb() {
//    }
//
//    private static final class Holder {
//        private static final AccounDb INSTANCE = new AccounDb();
//    }
//
//    public static AccounDb getIntance() {
//        return Holder.INSTANCE;
//    }
//
//    public void insert(AccountModel model) {
//        DataBaseManager.getIntance().getAccountDao().insertOrReplace(model);
//    }
//}
