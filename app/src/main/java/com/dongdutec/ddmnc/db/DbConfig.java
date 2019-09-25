package com.dongdutec.ddmnc.db;

import android.content.Context;

import com.dongdutec.ddmnc.db.model.User;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class DbConfig {
    public Context context;

    public DbConfig(Context context) {
        this.context = context;
    }

    public DbManager.DaoConfig getDaoConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("ddmnc.db")
                .setAllowTransaction(true)
                .setDbDir(context.getFilesDir())
                //  .setDbDir(Environment.getExternalStorageDirectory())
                .setDbVersion(1);

        return daoConfig;
    }

    public DbManager getDbManager() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        return db;
    }

    public User getUserByPhone(String phone) {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getPhone().equals(phone)) {
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public User getUser() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        return user;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public Boolean getIsLogin() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        return true;
                    }
                }
            }

        } catch (DbException e) {
        }
        return false;
    }

    public String getToken() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        String token = user.getToken();
                        return token;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    public int getId() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        int id = user.getId();
                        return id;
                    }
                }
            }

        } catch (DbException e) {
        }
        return 0;
    }

    public double getLatitude() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        double latitude = user.getLatitude();
                        return latitude;
                    }
                }
            }

        } catch (DbException e) {
        }
        return 0;
    }
    public double getLongitude() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        double longitude = user.getLongitude();
                        return longitude;
                    }
                }
            }

        } catch (DbException e) {
        }
        return 0;
    }

    public String getPhone() {
        DbManager.DaoConfig daoConfig = getDaoConfig();
        DbManager db = x.getDb(daoConfig);
        try {
            List<User> users = db.selector(User.class)
                    .findAll();
            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (user.getIsLogin().equals("1")) {
                        String phone = user.getPhone();
                        return phone;
                    }
                }
            }

        } catch (DbException e) {
        }
        return null;
    }

    /*

     User user = new User();
        try {
            user.setId(data.getInt("id"));
            user.setStoreName(data.getString("storeName"));
            user.setStatus(data.getInt("status"));
            user.setStoreImgUrl(data.getString("storeImgUrl"));
            user.setStoreLoginName(data.getString("storeLoginName"));
            user.setToken(data.getString("token"));
            user.setUpdateTime(data.getString("updateTime"));
            user.setPhone(data.getString("phone"));
            user.setIsVoice("1");//语音播报 初始化默认为1开启语音播报
            user.setIsLogin("1");
            DbConfig dbConfig = new DbConfig(getApplicationContext());
            DbManager db = dbConfig.getDbManager();


            db.saveOrUpdate(user);

    * */
}