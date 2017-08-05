package com.kareem.newme.Model.RealmObjects;

import android.content.Context;
import android.support.annotation.Nullable;

import com.kareem.newme.Model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by kareem on 8/5/17.
 */

public class RealmTokenUtils {
    private Realm realm;

    public RealmTokenUtils(Context context) {
        realm = Realm.getInstance(new RealmConfiguration.Builder(context).name("fireBase_Token").build());
    }

    public RealmTokenUtils save(Token token) {
        clearRealmItems();
        realm.beginTransaction();
        realm.copyToRealm(token);
        realm.commitTransaction();
        realm.close();
        return this;
    }

    public void clearRealmItems() {
        RealmResults<Token> realmResults = realm.where(Token.class).findAll();
        realm.beginTransaction();
        realmResults.clear();
        realm.commitTransaction();
    }

    @Nullable
    public Token getTokenFromDataBase() {
        RealmResults<Token> tokens = realm.where(Token.class).findAll();
        if (tokens.size() > 0) {
            return tokens.get(0);
        } else return null;
    }
}