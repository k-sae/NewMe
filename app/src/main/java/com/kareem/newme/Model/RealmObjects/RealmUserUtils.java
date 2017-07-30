package com.kareem.newme.Model.RealmObjects;

import android.content.Context;
import android.support.annotation.Nullable;

import com.kareem.newme.Model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by kareem on 7/30/17.
 */

public class RealmUserUtils{
    private Realm realm;
    public RealmUserUtils(Context context) {
        realm = Realm.getInstance(new RealmConfiguration.Builder(context).name("Login_Credentials").build());
    }

    public User fromRealmUser(RealmUser realmUser)
    {
        User user = new User();
        user.setId(realmUser.getId());
        user.setName(realmUser.getName());
        user.setUserType(realmUser.getUserType());
        return user;
    }
    public RealmUser toRealmUser(User user)
    {
        RealmUser realmUser = new RealmUser();
        realmUser.setUserType(user.getUserType());
        realmUser.setName(user.getName());
        realmUser.setId(user.getId());
        return realmUser;
    }
    public void save(User user)
    {
        clearRealmItems();
        realm.beginTransaction();
        realm.copyToRealm( toRealmUser(user));
        realm.commitTransaction();
        realm.close();
    }
    public void  clearRealmItems()
    {
        RealmResults<RealmUser> realmResults = realm.where(RealmUser.class).findAll();
        realm.beginTransaction();
        realmResults.clear();
        realm.commitTransaction();
    }

    @Nullable
    public User getLoggedUserFromDataBase()
    {
        RealmResults<RealmUser> realmUsers = realm.where(RealmUser.class).findAll();
        if (realmUsers.size() > 0) {
            return fromRealmUser(realmUsers.get(0));
        }
        else return null;
    }
}
