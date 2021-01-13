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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static net.vplaygames.VPlayGames.PokeMasDB.Entities.Constant.EMPTY_POKEMON;

public class TrainerData extends AbstractTrainer implements PVJObject<TrainerData>
{
    public final JSONArray<Pokemon> pokemonData;

    public TrainerData(Trainer trainer) {
        super(trainer.name,trainer.rarity,trainer.img,trainer.data,trainer.pokemon);
        pokemonData = new JSONArray<>();
    }

    public TrainerData add(Pokemon pokemon) {
        pokemonData.add(pokemon);
        return this;
    }

    @Override
    public String getAsJSON() {
        return super.getAsJSON().substring(0,super.getAsJSON().length()-1)+",\"pokemonData\":"+pokemonData.getAsJSON()+"}";
    }

    @Override
    public String toString() {
        return getAsJSON();
    }

    @Override
    public TrainerData parseFromJSON(String JSON) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        return parse(JSON);
    }

    public static TrainerData parse(String json) throws net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException {
        org.json.simple.JSONObject jo;
        try {
            jo = (org.json.simple.JSONObject) new JSONParser().parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new net.vplaygames.VPlayGames.PokeMasDB.Exceptions.ParseException();
        }
        TrainerData trainerData = new TrainerData(Trainer.parse(json));
        JSONArray.parseFromJSON((org.json.simple.JSONArray) jo.get("pokemonData"), EMPTY_POKEMON)
                .iterator()
                .forEachRemaining(trainerData::add);
        return trainerData;
    }

    public static TrainerData emptyTrainerData() {
        return new TrainerData(Trainer.emptyTrainer()).add(Pokemon.emptyPokemon());
    }
}
