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

import net.vplaygames.VPlayGames.PokeMasDB.Entities.Pokemon;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.TrainerData;
import net.vplaygames.VPlayGames.PokeMasDB.Util.Array;

import java.util.ArrayList;
import java.util.Set;

public class PokemonDataCache extends Cache<ArrayList<Pokemon>> {
    private static PokemonDataCache instance;

    private PokemonDataCache() {
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (TrainerData td : tdc.DataCache) {
            for (Pokemon p : td.pokemonData) {
                tdc.totalProcessed++;
                if (!this.containsKey(p.name)) {
                    this.put(p.name, new ArrayList<>());
                }
                ArrayList<Pokemon> result = this.get(p.name);
                result.add(p);
                this.put(p.name, result);
            }
        }

        Set<String> keys = this.keySet();

        for (String name1 : keys) {
            for (String name2 : keys) {
                if (name2.contains(name1)            //check if it's a form of the given pokemon
                        && !name2.equals(name1)      //check if it's not the same pokemon; saves from duplicates
                        // other exceptions
                        && !Array.contains(name2, tdc.get("Player").pokemon)
                        && !name1.equals("Mew")) {
                    ArrayList<Pokemon> result = this.get(name2);
                    result.addAll(this.get(name1));
                    this.put(name1, result);
                    this.put(name2, result);
                }
            }
        }
    }

    public static PokemonDataCache getInstance() {
        return instance != null ? instance : (instance = new PokemonDataCache());
    }
}