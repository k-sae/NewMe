package com.kareem.newme.services;


        import com.android.volley.VolleyError;
        import com.google.firebase.iid.FirebaseInstanceId;
        import com.kareem.newme.Connections.VolleyRequest;
        import com.kareem.newme.Constants;
        import com.kareem.newme.Model.RealmObjects.RealmTokenUtils;
import com.kareem.newme.Model.RealmObjects.FireBaseToken;

        import java.util.HashMap;
        import java.util.Map;

public class NewMeInstanceIDService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FireBaseToken fireBaseToken = new FireBaseToken(refreshedToken);
         new RealmTokenUtils(this).save(fireBaseToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "addDevice");
        stringMap.put("deviceToken",token);
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendRegistrationToServer(token);
            }

            @Override
            public void onResponse(String response) {
            }
        };
        volleyRequest.start();
    }
}