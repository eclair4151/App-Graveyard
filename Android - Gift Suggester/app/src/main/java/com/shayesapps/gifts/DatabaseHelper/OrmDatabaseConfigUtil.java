package com.shayesapps.gifts.DatabaseHelper;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.shayesapps.gifts.Models.Gift;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by tomershemesh on 8/28/14.
 */
public class OrmDatabaseConfigUtil extends OrmLiteConfigUtil {

    private static  final Class<?>[] classes = new Class[]{Gift.class};

    public static void main(String[] args) throws SQLException,IOException
    {
        writeConfigFile(new File("app/src/main/res/raw/ormlite_config.txt"),classes);
    }
}
