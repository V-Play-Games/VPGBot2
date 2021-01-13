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

public class Constant<E> {
    public static final Move EMPTY_MOVE=Move.emptyMove();

    public static final Passive EMPTY_PASSIVE=Passive.emptyPassive();

    public static final Pokemon EMPTY_POKEMON=Pokemon.emptyPokemon();

    public static final StatRange EMPTY_STAT_RANGE=StatRange.emptyStatRange();

    public static final Stats EMPTY_STATS=Stats.emptyStats();

    public static final SyncGridNode EMPTY_SYNC_GRID_NODE=SyncGridNode.emptySyncGridNode();

    public static final SyncMove EMPTY_SYNC_MOVE=SyncMove.emptySyncMove();

    public static final Trainer EMPTY_TRAINER=Trainer.emptyTrainer();

    public static final TrainerData EMPTY_TRAINER_DATA=TrainerData.emptyTrainerData();

    public final E value;

    private Constant(E value) {
        this.value=value;
    }
}
