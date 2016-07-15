package ru.alexkulikov.smartvalentine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.vk_auth);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, "audio");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                sendRequest();
            }

            @Override
            public void onError(VKError error) {

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendRequest() {
        final VKRequest request = VKApi.audio().get(VKParameters.from(VKApiConst.OWNER_ID, "42515731"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Map<String, Integer> genreMap = new HashMap<>();
                try {
                    JSONArray jsonAudio = response.json.getJSONObject("response").getJSONArray("items");
                    for (int i = 0; i < jsonAudio.length(); i++) {
                        JSONObject audio = jsonAudio.getJSONObject(i);
                        if (audio.has("genre_id")) {
                            int genre = audio.getInt("genre_id");
                            String genreName = Genre.getName(genre);
                            int count = genreMap.containsKey(genreName) ? genreMap.get(genreName) : 0;
                            genreMap.put(genreName, count + 1);
                        }
                    }

                    Log.i("S","S");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {


            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {


            }
        });
    }
}
