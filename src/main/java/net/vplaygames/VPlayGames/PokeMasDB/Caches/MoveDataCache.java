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

import net.vplaygames.VPlayGames.PokeMasDB.Entities.Move;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.Pokemon;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.TrainerData;

import java.util.ArrayList;

public class MoveDataCache extends Cache<MoveDataCache.Node> {
    private static MoveDataCache instance;

    private MoveDataCache() {
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (TrainerData td : tdc.DataCache) {
            for (Pokemon p : td.pokemonData) {
                for (Move m : p.moves) {
                    if (!this.containsKey(m.name)) {
                        this.put(m.name, new Node(m));
                    }
                    this.put(m.name, this.get(m.name).add(p));
                }
            }
        }
    }

    public static MoveDataCache getInstance() {
        return instance != null ? instance : (instance = new MoveDataCache());
    }

    public static class Node {
        public final Move move;
        private final ArrayList<String> users;
        private final ArrayList<String> simpleUserNames;
        private final ArrayList<String> simpleUserTrainerNames;

        public Node(Move move) {
            this.move=move;
            users = new ArrayList<>();
            simpleUserNames = new ArrayList<>();
            simpleUserTrainerNames = new ArrayList<>();
        }

        public Object[] getUsers() {
            return users.toArray();
        }

        public Object[] getSimpleUserNames() {
            return simpleUserNames.toArray();
        }

        public Object[] getSimpleUserTrainerNames() {
            return simpleUserTrainerNames.toArray();
        }

        Node add(Pokemon p) {
            int len = users.size();
            simpleUserTrainerNames.add(len, p.trainer);
            simpleUserNames.add(len, p.name);
            users.add(len, p.trainer + "'s " + p.name);
            return this;
        }
    }
}