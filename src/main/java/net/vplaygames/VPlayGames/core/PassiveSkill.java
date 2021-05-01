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

import com.vplaygames.PM4J.caches.Cache;
import com.vplaygames.PM4J.exceptions.ParseException;
import com.vplaygames.PM4J.json.ParsableJSONObject;
import com.vplaygames.PM4J.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import org.json.simple.JSONObject;

public class PassiveSkill implements ParsableJSONObject {
    public final String name;
    public final String condition;
    public final boolean intensive;
    public final int checks;
    public final int modifies;

    public PassiveSkill(String name, String condition, boolean intensive, int checks, int modifies) {
        this.name = name;
        this.condition = condition;
        this.intensive = intensive;
        this.checks = checks;
        this.modifies = modifies;
        Data.instance.put(name, this);
    }

    @Override
    public String getAsJSON() {
        return "{\"condition\":\"" + condition + "\", \"intensive\":" + intensive + ", \"checks\":" + checks + ", \"modifies\":" + modifies + "}";
    }

    public static PassiveSkill parse(String JSON) throws ParseException {
        return parse(MiscUtil.parseJSONObject(JSON));
    }

    public static PassiveSkill parse(JSONObject jo) {
        String name = (String) jo.get("name");
        String condition = (String) jo.get("condition");
        boolean intensive = (boolean) jo.get("intensive");
        int modifies = MiscUtil.objectToInt(jo.get("modifies"));
        int checks = MiscUtil.objectToInt(jo.get("checks"));
        return new PassiveSkill(name, condition, intensive, checks, modifies);
    }

    public static PassiveSkill get(String skill) {
        return Data.instance.get(Strings.reduceToAlphabets(skill));
    }

    public static class Data extends Cache<PassiveSkill> {
        public static Data instance = new Data();
    }
}
