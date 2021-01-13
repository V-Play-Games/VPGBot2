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

import net.vplaygames.VPlayGames.PokeMasDB.Entities.Passive;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.Pokemon;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.SyncGridNode;
import net.vplaygames.VPlayGames.PokeMasDB.Entities.TrainerData;

import java.util.ArrayList;

public class SkillDataCache extends Cache<SkillDataCache.Node> {
    private static SkillDataCache instance;
    private Pokemon p;
    private Passive pass;

    private SkillDataCache() {
        TrainerDataCache tdc = TrainerDataCache.getInstance();
        for (int i1 = 0; i1<tdc.DataCache.size(); i1++) {
            TrainerData td = tdc.DataCache.get(i1);
            for (int i2 = 0; i2< td.pokemonData.size(); i2++) {
                p = td.pokemonData.get(i2);
                for (int i3=0; i3<p.passives.size(); i3++) {
                    pass = p.passives.get(i3);
                    computeSkill(false);
                    computeSkillGroup(false);
                }
                for (int i3 = 0; i3<p.grid.size(); i3++) {
                    SyncGridNode sgn = p.grid.get(i3);
                    if (sgn.title.equals(sgn.description)) continue;
                    if (sgn.title.contains(":")) {
                        pass = new Passive(sgn.title.split(":")[1], sgn.description);
                        computeSkill(true);
                        computeSkillGroup(true);
                    }
                    pass = new Passive(sgn.title.replace(":",": "),sgn.description);
                    computeSkill(true);
                    computeSkillGroup(true);
                }
            }
        }
    }

    private void computeSkillGroup(boolean isGrid) {
        if (Character.isDigit(pass.name.charAt(pass.name.length()-1))) {
            String newName = pass.name.substring(0,pass.name.length()-2);
            pass = new Passive(newName, "This is a group of passive skills "+newName+" 1-9.");
            computeSkill(isGrid);
        }
    }

    private void computeSkill(boolean isGird) {
        if (!this.containsKey(pass.name)) {
            this.put(pass.name, new Node(pass));
        }
        this.put(pass.name, this.get(pass.name).add(p,isGird));
    }

    public static SkillDataCache getInstance() {
        return instance != null ? instance : (instance = new SkillDataCache());
    }

    public static class Node {
        public final Passive skill;
        private final ArrayList<String> inbuilt;
        private final ArrayList<String> inGrid;

        public Node(Passive skill) {
            this.skill=skill;
            inbuilt = new ArrayList<>();
            inGrid = new ArrayList<>();
        }

        public Object[] getInbuilt() {
            return inbuilt.toArray();
        }

        public Object[] getInGrid() {
            return inGrid.toArray();
        }

        Node add(Pokemon p, boolean isGrid) {
            String toPut = p.trainer + "'s " + p.name;
            if (isGrid) {
                for (String ig : inGrid) {
                    if (ig.equals(toPut)) {
                        return this;
                    }
                }
                inGrid.add(toPut);
            } else {
                for (String ib : inGrid) {
                    if (ib.equals(toPut)) {
                        return this;
                    }
                }
                inbuilt.add(toPut);
            }
            return this;
        }
    }
}
