package com.kareem.newme.Model.RealmObjects;

import android.content.Context;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by kareem on 8/5/17.
 */

public class RealmTokenUtils {
    private Realm realm;

    public RealmTokenUtils(Context context) {
        realm = Realm.getInstance(new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .name("fireBase_Token")
                .build());
    }

    public RealmTokenUtils save(FireBaseToken fireBaseToken) {
        clearRealmItems();
        realm.beginTransaction();
        realm.copyToRealm(fireBaseToken);
        realm.commitTransaction();
        realm.close();
        return this;
    }

    public void clearRealmItems() {
        RealmResults<FireBaseToken> realmResults = realm.where(FireBaseToken.class).findAll();
        realm.beginTransaction();
        realmResults.clear();
        realm.commitTransaction();
    }

    @Nullable
    public FireBaseToken getTokenFromDataBase() {
        RealmResults<FireBaseToken> tokens = realm.where(FireBaseToken.class).findAll();
        if (tokens.size() > 0) {
            return tokens.get(0);
        } else return null;
    }
}