/*
 * Copyright 2020-2021 Vaibhav Nargwani
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
package net.vplaygames.VPlayGames.commands.fun.meme;

import com.vplaygames.PM4J.json.ParsableJSONObject;
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class Meme implements ParsableJSONObject {
    public final String postLink;
    public final String subreddit;
    public final String title;
    public final String url;
    public final boolean nsfw;
    public final boolean spoiler;
    public final String author;
    public final int ups;
    public final String[] previews;

    public Meme(String postLink, String subreddit, String title, String url, boolean nsfw, boolean spoiler, String author, int ups, String[] previews) {
        this.postLink = postLink;
        this.subreddit = subreddit;
        this.title = title;
        this.url = url;
        this.nsfw = nsfw;
        this.spoiler = spoiler;
        this.author = author;
        this.ups = ups;
        this.previews = previews;
    }

    public static Meme parse(String json) {
        JSONObject jo    = MiscUtil.parseJSONObject(json);
        int ups          = MiscUtil.objectToInt(jo.get("ups"));
        boolean nsfw     = (boolean)jo.get("nsfw");
        boolean spoiler  = (boolean)jo.get("spoiler");
        String postLink  = (String) jo.get("postLink");
        String subreddit = (String) jo.get("subreddit");
        String title     = (String) jo.get("title");
        String url       = (String) jo.get("url");
        String author    = (String) jo.get("author");
        return new Meme(postLink, subreddit, title, url, nsfw, spoiler, author, ups, new String[0]);
    }

    @Override
    public String getAsJSON() {
        return "{\"postLink\":\"" + postLink +
                "\",\"subreddit\":\" " + subreddit +
                "\",\"title\":\"" + title +
                "\",\"url\":\"" + url +
                "\",\"nsfw\":" + nsfw +
                ",\"spoiler\":" + spoiler +
                ",\"author\":\"" + author +
                "\",\"ups\":" + ups +
                ",\"previews\":" + Arrays.deepToString(previews)+"}";
    }
}
