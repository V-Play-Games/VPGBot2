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
package net.vplaygames.VPlayGames.PokeMasDB.Caches;

import java.util.HashMap;

import static net.vplaygames.VPlayGames.data.Bot.BASE64;

public class Cache<T> extends HashMap<String, T> {
    protected Cache() {}

    @Override
    public T get(Object key) {
        if (!(key instanceof String)) {
            return null;
        }
        String temp = (String) key;
        for (String k : this.keySet()) {
            if (reduceToAlphanumeric(k).equalsIgnoreCase(reduceToAlphanumeric(temp))) {
                return super.get(k);
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String)) {
            return false;
        }
        String temp = (String) key;
        for (String k : this.keySet()) {
            if (reduceToAlphanumeric(k).equalsIgnoreCase(reduceToAlphanumeric(temp))) {
                return true;
            }
        }
        return false;
    }

    private static String reduceToAlphanumeric(String s) {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i<s.length(); i++) {
            if (BASE64.substring(0,BASE64.length()-2).contains(Character.toString(s.charAt(i)))) {
                tor.append(s.charAt(i));
            }
        }
        return tor.toString();
    }

    public enum Type {
        TRAINER("Trainer", true, TrainerDataCache.getInstance()),
        POKEMON("Pokemon", true, PokemonDataCache.getInstance()),
        SKILL("Passive Skill", false, SkillDataCache.getInstance()),
        MOVE("Move", false, MoveDataCache.getInstance()),
        UNKNOWN(null, false, null);

        private final String value;
        private final boolean turnable;
        private final Cache<?> cache;

        Type(String value, boolean turnable, Cache<?> cache) {
            this.value = value;
            this.turnable = turnable;
            this.cache = cache;
        }

        @Override
        public String toString() {
            return value;
        }

        public boolean isTurnable() {
            return turnable;
        }

        public Cache<?> getCache() {
            return cache;
        }

        public static Type parseType(String toParse) {
            switch (toParse.toLowerCase()) {
                case "trainer": return TRAINER;
                case "pokemon": return POKEMON;
                case "move":    return MOVE;
                case "passive":
                case "skill":   return SKILL;
                default:        return UNKNOWN;
            }
        }

        public static boolean isType(String toParse) {
            return parseType(toParse) != UNKNOWN;
        }
    }
}