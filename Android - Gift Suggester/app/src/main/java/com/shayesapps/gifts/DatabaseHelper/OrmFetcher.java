package com.shayesapps.gifts.DatabaseHelper;

import android.content.Context;
import android.util.Log;

import com.shayesapps.gifts.Models.Gift;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by tomershemesh on 8/28/14.
 */
public class OrmFetcher{
    private static OrmHelper ormHelper;
    public static void init(Context context)
    {
        ormHelper = new OrmHelper(context);
    }

    //add a gift to the local database
    public static void saveGift(Gift gift) {
        try {
            ormHelper.getGiftDao().createOrUpdate(gift);
        } catch (SQLException e) {
            Log.e("SQL Error saving gifts",e.getSQLState());
        }
    }

    //get all gifts in the database
    public static List<Gift> getGifts()  {
        List<Gift> gifts = null;
        try {
            gifts = ormHelper.getGiftDao().queryForAll();
        } catch (SQLException e) {
            if(e!=null)
            {
                Log.e("SQL Error fetching gifts",e.getMessage());
            }
            else
            {
                Log.e("SQL Error fetching gifts","e is also null");
            }
        }
        return gifts;
    }

    //delete gift from database
    public static void deleteGift(Gift gift)
    {
        try {
            ormHelper.getGiftDao().deleteById(gift._id);
        } catch (SQLException e) {
            Log.e("SQL Error deleting gifts",e.getSQLState());
        }
    }
}
