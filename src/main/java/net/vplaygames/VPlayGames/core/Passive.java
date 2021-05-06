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
import org.json.simple.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class Passive {
    static Pattern digits = Pattern.compile("\\d+");
    static Pattern noDigits = Pattern.compile("\\D+");
    public final Skill skill;
    public final int intensity;

    public Passive(Skill skill, int intensity) {
        this.skill = skill;
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return skill.name + (skill.intensive ? " " + intensity : "");
    }

    public static Passive of(String s) {
        int intensity = 0;
        try {
            Matcher digits = Passive.digits.matcher(s);
            digits.find();
            intensity = Integer.parseInt(s.substring(digits.start(), digits.end()));
        } catch (Exception ignored) {
        }
        Matcher noDigits = Passive.noDigits.matcher(s);
        noDigits.find();
        Skill skill = Skill.get(s.substring(noDigits.start(), noDigits.end()));
        return new Passive(skill, intensity);
    }

    public static class Skill implements ParsableJSONObject {
        public static Cache<Skill> passives = new Cache<>();
        public final String name;
        public final String condition;
        public final boolean intensive;
        public final int checks;
        public final int modifies;

        public Skill(String name, String condition, boolean intensive, int checks, int modifies) {
            this.name = name;
            this.condition = condition;
            this.intensive = intensive;
            this.checks = checks;
            this.modifies = modifies;
            passives.put(name, this);
        }

        @Override
        public String getAsJSON() {
            return "{\"condition\":\"" + condition + "\", \"intensive\":" + intensive + ", \"checks\":" + checks + ", \"modifies\":" + modifies + "}";
        }

        public static Skill parse(String JSON) throws ParseException {
            return parse(MiscUtil.parseJSONObject(JSON));
        }

        public static Skill parse(JSONObject jo) {
            String name = (String) jo.get("name");
            String condition = (String) jo.get("condition");
            boolean intensive = (boolean) jo.get("intensive");
            int modifies = MiscUtil.objectToInt(jo.get("modifies"));
            int checks = MiscUtil.objectToInt(jo.get("checks"));
            return new Skill(name, condition, intensive, checks, modifies);
        }

        public static Skill get(String skill) {
            return passives.get(skill);
        }
    }
}
