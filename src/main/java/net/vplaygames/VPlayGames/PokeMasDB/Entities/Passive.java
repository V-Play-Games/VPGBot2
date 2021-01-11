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
package net.vplaygames.VPlayGames.PokeMasDB.Entities;

import net.vplaygames.VPlayGames.PokeMasDB.JSONFramework.PVJObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Passive implements PVJObject<Passive>
{
    public final String name;
    public final String description;

    public Passive(String name,String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getAsJSON() {
        return "{"+"\"name\":\""+name+"\",\"description\":\""+description+"\"}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Passive parseFromJSON(String JSON) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        return parse(JSON);
    }

    public static Passive parse(String json) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        JSONObject jo;
        try {
            jo = (JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            throw new net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException();
        }
        String name = (String) jo.get("name");
        String description = (String) jo.get("description");
        return new Passive(name,  description);
    }

    public static Passive emptyPassive() {
        return new Passive("","");
    }
}
