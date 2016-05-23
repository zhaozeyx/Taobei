package com.hengrtec.taobei.database.adapter;

import android.content.Context;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by zhaozeyang on 15/7/1.
 */
public class RealmFactory {

  private static final String DB_NAME = "taobei";

  private static final String DEFAULT_DB_NAME = "default";

  private static final String DB_SUFFIX = ".realm";

  public static Realm getRealm(Context context) {
    RealmConfiguration config = new RealmConfiguration.Builder(context).name(DB_NAME +
        DB_SUFFIX)
        .migration(new AppMigration())
        .build();
    return Realm.getInstance(config);
  }

  public static void initRealm(Context context) {
    RealmConfiguration config = new RealmConfiguration.Builder(context).name(DB_NAME +
        DB_SUFFIX)
        .migration(new AppMigration())
        .build();
    Realm.setDefaultConfiguration(config);
  }

  private static class AppMigration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }
  }
}
