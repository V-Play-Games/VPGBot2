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
package net.vplaygames.VPlayGames.PokeMasDB.JSONFramework;

import net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class JSONArray<E extends PVJObject<E>> extends ArrayList<E> implements List<E>, PVJObject<JSONArray<E>> {
    private E dummyElement;

    public JSONArray(int count, E element) {
        dummyElement=element;
        while (count-->0)
            this.add(element);
    }

    public JSONArray() {}

    @Override
    public String getAsJSON() {
        StringBuilder tor = new StringBuilder();
        boolean first = true;
        for (E e : this) {
            if (first) {
                first = false;
            } else {
                tor.append(',');
            }
            tor.append(e.getAsJSON());
        }
        return "[" + tor.toString() + "]";
    }

    @Override
    public JSONArray<E> parseFromJSON(String JSON) throws ParseException {
        try {
            return parseFromJSON((org.json.simple.JSONArray) new JSONParser().parse(JSON), dummyElement);
        } catch (org.json.simple.parser.ParseException e) {
            throw new ParseException();
        }
    }

    @Override
    public void validate() {
        this.forEach(PVJObject::validate);
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    public static <E extends PVJObject<E>>
    JSONArray<E> parseFromJSON(org.json.simple.JSONArray ja, E element) {
        JSONArray<E> jsonArray = new JSONArray<>();
        for (Object object : ja) {
            jsonArray.add(element.parseFromJSON(object.toString()));
        }
        return jsonArray;
    }
}