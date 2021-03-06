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

abstract class AbstractMove
{
    public final String name;
    public final String type;
    public final String category;
    public final int minPower;
    public final String target;
    final String effectAtForce; //to be replaced by child class
    public final String[] unlockRequirements = new String[0];

    AbstractMove(String name, String type, String category,
                 int minPower,
                 String target, String effectAtForce) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.minPower = minPower;
        this.target = target;
        this.effectAtForce = effectAtForce;
    }
}
