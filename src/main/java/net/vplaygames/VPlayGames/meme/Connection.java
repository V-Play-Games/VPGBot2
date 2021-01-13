/*
 * Copyright 2020 Vaibhav Nargwani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.vplaygames.VPlayGames.meme;

import com.vplaygames.PM4J.exceptions.ConnectionClosedException;
import com.vplaygames.PM4J.jsonFramework.JSONArray;
import com.vplaygames.PM4J.util.MiscUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

public class Connection implements Closeable {
    OkHttpClient client;
    final static String MEME_URL_ENDPOINT = "https://meme-api.herokuapp.com/gimme/";

    public Connection() {
        client = new OkHttpClient();
    }

    public Meme getMeme() throws IOException {
        return getMeme("");
    }

    public Meme getMeme(String subReddit) throws IOException {
        return getMemes(0, subReddit).get(0);
    }

    public ArrayList<Meme> getMemes(int amount) throws IOException {
        return getMemes(amount, "");
    }

    public ArrayList<Meme> getMemes(int amount, String subReddit) throws IOException {
        String tor;
        String url = MEME_URL_ENDPOINT + ("".equals(subReddit) ? "" : subReddit + "/") + (amount < 1 ? "" : amount);
        try (Response response = requestData(url)) {
            if (response.code() >= 400) {
                throw new IOException("An unexpected error has occurred! " + url + " returned HTTP Code " + response.code());
            }
            tor = response.body() == null ? "" : response.body().string();
        }
        JSONObject jo = MiscUtil.parseJSONObject(tor);
        JSONArray<Meme> ja;
        if (jo.containsKey("memes")) {
            ja = JSONArray.parse((org.json.simple.JSONArray) jo.get("memes"), Meme.noMeme());
        } else {
            ja = new JSONArray<>(1, Meme.parse(tor));
        }
        return ja;
    }

    private Response requestData(String url) throws IOException {
        if (client == null)
            throw new ConnectionClosedException();
        return client.newCall(new Request.Builder().url(url).build()).execute();
    }

    @Override
    public void close() {
        client = null;
    }
}
