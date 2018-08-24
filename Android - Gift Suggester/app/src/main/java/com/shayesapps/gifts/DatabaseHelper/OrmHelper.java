package com.shayesapps.gifts.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.shayesapps.gifts.Models.Gift;
import com.shayesapps.gifts.R;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomershemesh on 8/28/14.
 */

//this entire class is the generic ormhelper, as shown in the orm library, most of this is not mine
public class OrmHelper extends OrmLiteSqliteOpenHelper{
    private static final String DATABASE_NAME = "savedGifts.db";
    private static final int DATABASE_VERSION = 4;

    private Dao<Gift ,Integer> giftDao = null;
    private RuntimeExceptionDao<Gift, Integer> giftRunTime;

    public  OrmHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION,R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Gift.class);
        } catch (SQLException e) {
            Log.e("error creating table",e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Gift.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            Log.e("error creating table", e.getMessage());
        }
    }

    public Dao<Gift, Integer> getGiftDao() throws SQLException {
        if(giftDao == null)
        {
            giftDao = getDao(Gift.class);
        }
        return giftDao;
    }

    public RuntimeExceptionDao<Gift,Integer> getGiftRunTime()
    {
        if(giftRunTime == null)
        {
            giftRunTime = getRuntimeExceptionDao(Gift.class);
        }
        return giftRunTime;
    }
}
