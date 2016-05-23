package com.hengrtec.taobei.database.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zhaozeyang on 15/7/2.
 */
public abstract class DbAdapter<T extends RealmObject> {
  private static final String TAG = "DbHelper";

  private Realm mRealm;

  public DbAdapter(Context context) {
    mRealm = RealmFactory.getRealm(context);
  }

  public void create(T object) {
    mRealm.beginTransaction();
    mRealm.copyToRealm(object);
    mRealm.commitTransaction();
  }

  public void createOrUpdate(T object) {
    mRealm.beginTransaction();
    mRealm.copyToRealmOrUpdate(object);
    mRealm.commitTransaction();
  }

  public void delete(T object) {
    mRealm.beginTransaction();
    object.deleteFromRealm();
    mRealm.commitTransaction();
  }

  public void delete(RealmResults results, int index) {
    mRealm.beginTransaction();
    results.remove(index);
    mRealm.commitTransaction();
  }

  public RealmResults<T> findAll() {
    return mRealm.where(getObjectClass()).findAll();
  }

  public RealmResults<T> findAllSorted(String fieldName, boolean sortAscending) {
    return mRealm.where(getObjectClass()).findAllSorted(fieldName, sortAscending ? Sort
        .ASCENDING : Sort.DESCENDING);
  }

  public T getObject(String keyColumns, Object key) {
    if (TextUtils.isEmpty(keyColumns) || "".equals(key)) {
      Log.e(TAG, "keyColumns is empty or key is null");
      return null;
    }
    RealmQuery<T> query = mRealm.where(getObjectClass());
    if (key instanceof String) {
      return query.equalTo(keyColumns, (String) key).findFirst();
    } else if (key instanceof Integer) {
      return query.equalTo(keyColumns, (Integer) key).findFirst();
    } else if (key instanceof Float) {
      return query.equalTo(keyColumns, (Float) key).findFirst();
    } else if (key instanceof Long) {
      return query.equalTo(keyColumns, (Long) key).findFirst();
    }
    // TODO 其它类型
    return null;
  }

  protected abstract Class<T> getObjectClass();

  public void close() {
    mRealm.close();
  }
}
