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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button regButton = (Button) findViewById(R.id.vk_auth);
        Button goButton = (Button) findViewById(R.id.vk_go);

        assert regButton != null && goButton != null;
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, "audio", "groups");
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGroupRequest();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

            }

            @Override
            public void onError(VKError error) {

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendAudioRequest() {
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

                    Log.i("S", "S");
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

    private void sendGroupRequest() {
        final VKRequest request = VKApi.groups().get(VKParameters.from(VKApiConst.USER_ID, "17764970", VKApiConst.EXTENDED, 1, VKApiConst.FIELDS, "description"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Map<String, Integer> keyMap = new HashMap<>();
                try {
                    JSONArray jsonGroup = response.json.getJSONObject("response").getJSONArray("items");
                    for (int i = 0; i < jsonGroup.length(); i++) {
                        JSONObject group = jsonGroup.getJSONObject(i);

                        String[] names = group.getString("name").toLowerCase().split(" ");
                        String[] descs = group.getString("description").toLowerCase().split(" ");

                        for (String name : names) {
                            if (name.length() < 5) continue;
                            int count = keyMap.containsKey(name) ? keyMap.get(name) : 0;
                            keyMap.put(name, count + 1);
                        }

                        for (String desc : descs) {
                            if (desc.length() < 5) continue;
                            int count = keyMap.containsKey(desc) ? keyMap.get(desc) : 0;
                            keyMap.put(desc, count + 1);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Map<String, Integer> orderMap = sortByValue(keyMap);
                Log.i("S","S");
            }
        });
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
