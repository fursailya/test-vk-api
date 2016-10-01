package com.vk.droid.fursa.test_vk_api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

public class MainActivity extends Activity {
    private String[] scope = new String[]{
            VKScope.FRIENDS
    };
    private ListView friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Получение отпечатка
        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));
        friendsList = (ListView)findViewById(R.id.musicListView);
        VKSdk.login(this, scope);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Auth - success", Toast.LENGTH_LONG).show();

                VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "first_name,last_name"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKList list = (VKList) response.parsedModel;
                        ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_expandable_list_item_1, list);

                        friendsList.setAdapter(friendsAdapter);

                    }
                });
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "Auth - eror", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
