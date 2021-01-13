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
package net.vplaygames.VPlayGames.PokeMasDB;

import net.vplaygames.VPlayGames.PokeMasDB.Entities.Trainer;
import net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ConnectionClosedException;
import net.vplaygames.VPlayGames.PokeMasDB.Exceptions.TrainerNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class Connection implements Closeable {
    OkHttpClient client;

    public Connection() {
        client = new OkHttpClient();
    }

    public String requestTrainers() throws IOException {
        Response response = requestData("https://pokemasdb.com/trainer","requestTrainer");
        if (response.code()>=400) {
            throw new IOException("An unexpected error has occurred!\n\thttps://pokemasdb.com/trainer returned Error " + response.code());
        }
        String tor = response.body() == null ? "" : response.body().string();
        response.close();
        return tor;
    }

    public String requestTrainer(String trainer) throws IOException {
        Response response = requestData("https://pokemasdb.com/trainer/" + Trainer.resolve(trainer), "requestTrainer");
        if (response.code() >= 400) {
            throw new TrainerNotFoundException(response.code(), trainer);
        }
        String tor = response.body() == null ? "" : response.body().string();
        response.close();
        return tor;
    }

    public Response requestData(String url, String caller) throws IOException {
        if(client==null)
            throw new ConnectionClosedException("requestData"+((Objects.equals(caller,""))?"":"() called by "+caller));
        return client.newCall(new Request.Builder().url(new URL(url)).build()).execute();
    }

    public void close() {
        client=null;
    }
}