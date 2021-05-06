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

import com.vplaygames.PM4J.entities.Move;
import com.vplaygames.PM4J.entities.SyncMove;

public class Attack {
    public final String name;
    public final String type;
    public final String target;
    public final String effect;
    public final String category;
    public final int minPower;
    public final boolean isSync;

    public Attack(Move move) {
        this.name     = move.name;
        this.type     = move.type;
        this.target   = move.target;
        this.effect   = move.effect;
        this.category = move.category;
        this.minPower = move.minPower;
        this.isSync = false;
    }

    public Attack(SyncMove sync) {
        this.name     = sync.name + " (Sync Move)";
        this.type     = sync.type;
        this.target   = sync.target;
        this.effect   = sync.description;
        this.category = sync.category;
        this.minPower = sync.minPower;
        this.isSync = true;
    }
}
