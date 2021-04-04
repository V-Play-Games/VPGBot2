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
package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.caches.framework.Cache;

public class UserEmbedInfo {
    public final long uid;
    public final long embedId;
    public final String dataTitle;
    public final int limit;
    public final Cache.Type type;
    public int progress;

    public UserEmbedInfo(long uid, long embedId, String dataTitle, int limit, Cache.Type type) {
        this.uid = uid;
        this.embedId = embedId;
        this.dataTitle = dataTitle;
        this.limit = limit;
        this.type = type;
        this.progress = 0;
    }

    public boolean proceed(boolean add) {
        int oldProgress = progress;
        if (add)
            progress += (progress == limit) ? 0 : 1;
        else
            progress += (progress == 0) ? 0 : -1;
        return progress != oldProgress;
    }

    public void setProgress(int set) {
        this.progress = set;
    }

    public boolean isInRange(int set) {
        return set == Math.min(limit, Math.max(set, 0));
    }
}
