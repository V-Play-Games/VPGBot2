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

import net.vplaygames.VPlayGames.PokeMasDB.JSONFramework.JSONArray;
import net.vplaygames.VPlayGames.PokeMasDB.JSONFramework.PVJObject;
import net.vplaygames.VPlayGames.PokeMasDB.Util.Array;
import net.vplaygames.VPlayGames.util.Strings;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static net.vplaygames.VPlayGames.PokeMasDB.Entities.Constant.*;

public class Pokemon implements PVJObject<Pokemon>
{
    public final String name;
    public final String trainer;
    public final String[] typing;
    public final String weakness;
    public final String role;
    public final int rarity;
    public final String gender;
    public final String[] otherForms;
    public final JSONArray<Move> moves;
    public final SyncMove syncMove;
    public final JSONArray<Passive> passives;
    public final StatRange stats;
    public final JSONArray<SyncGridNode> grid;

    public Pokemon(String name, String trainer,
                   String[] typing,
                   String weakness, String role, String rarity, String gender,
                   String[] otherForms,
                   JSONArray<Move> moves, SyncMove syncMove,
                   JSONArray<Passive> passives, StatRange stats, JSONArray<SyncGridNode> grid) {
        this.name = name;
        this.trainer = trainer;
        this.typing = typing;
        this.weakness = weakness;
        this.role = role;
        this.rarity = rarity.equals("") ? 0 : Strings.toInt(rarity);
        this.gender = gender;
        this.otherForms = otherForms;
        this.moves = moves;
        this.syncMove = syncMove;
        this.passives = passives;
        this.stats = stats;
        this.grid = grid;
    }

    @Override
    public String getAsJSON() {
        return "{"+
                "\"name\":\""+name+"\","+
                "\"trainer\":\""+trainer+"\","+
                "\"typing\":["+ Array.toString(",",typing,"")+"],"+
                "\"weakness\":\""+weakness+"\","+
                "\"role\":\""+role+"\","+
                "\"rarity\":\""+rarity+"\","+
                "\"gender\":\""+gender+"\","+
                "\"otherForms\":["+Array.toString(",",otherForms,"")+"],"+
                "\"moves\":"+moves.getAsJSON()+","+
                "\"syncMove\":"+syncMove.getAsJSON()+","+
                "\"passives\":"+passives.getAsJSON()+","+
                "\"stats\":"+stats.getAsJSON()+","+
                "\"grid\":"+grid.getAsJSON()+
                "}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public Pokemon parseFromJSON(String JSON) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        return parse(JSON);
    }

    public static Pokemon parse(String json) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        org.json.simple.JSONObject jo;
        try {
            jo = (org.json.simple.JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            throw new net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException();
        }
        String name                  = (String) jo.get("name");
        String trainer               = (String) jo.get("trainer");
        String[] typing              = Array.toStringArray(((org.json.simple.JSONArray) jo.get("typing")).toArray());
        String weakness              = (String) jo.get("weakness");
        String role                  = (String) jo.get("role");
        String rarity                = (String) jo.get("rarity");
        String gender                = (String) jo.get("gender");
        String[] otherForms          = Array.toStringArray(((org.json.simple.JSONArray) jo.get("otherForms")).toArray());
        JSONArray<Move> moves        = JSONArray.parseFromJSON((org.json.simple.JSONArray) jo.get("moves"), EMPTY_MOVE);
        SyncMove syncMove            = SyncMove.parse((org.json.simple.JSONObject) jo.get("syncMove"));
        JSONArray<Passive> passives  = JSONArray.parseFromJSON((org.json.simple.JSONArray) jo.get("passives"), EMPTY_PASSIVE);
        StatRange stats              = StatRange.parse((org.json.simple.JSONObject) jo.get("stats"));
        JSONArray<SyncGridNode> grid = JSONArray.parseFromJSON((org.json.simple.JSONArray) jo.get("grid"), EMPTY_SYNC_GRID_NODE);
        return new Pokemon(name, trainer, typing, weakness, role, rarity, gender, otherForms, moves, syncMove, passives, stats, grid);
    }

    public static Pokemon emptyPokemon() {
        return new Pokemon("",
                "",
                new String[0],
                "",
                "",
                "",
                "",
                new String[0],
                new JSONArray<>(4, EMPTY_MOVE),
                EMPTY_SYNC_MOVE,
                new JSONArray<>(3, EMPTY_PASSIVE),
                EMPTY_STAT_RANGE,
                new JSONArray<>(7, EMPTY_SYNC_GRID_NODE));
    }
}