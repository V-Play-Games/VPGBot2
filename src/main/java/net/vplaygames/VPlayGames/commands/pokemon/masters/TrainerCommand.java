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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.Trainer;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.DamageAppCommand;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.core.Damage;

import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.core.Bot.DATA;
import static net.vplaygames.VPlayGames.core.Bot.PREFIX;

public class TrainerCommand extends DamageAppCommand {
    public TrainerCommand() {
        super("trainer");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        StringJoiner toSend = new StringJoiner("\n");
        Damage d = DATA.get(e.getAuthor().getIdLong());
        if (d.getAppStatus() == 0) {
            Trainer trainer = TrainerDataCache.getInstance().get(String.join("", e.getArgsFrom(1)));
            if (trainer == null)
                toSend.add("I cannot find any trainer with that name. Maybe this trainer is not usable in the game yet.");
            else {
                d.setTrainer(trainer)
                    .updateAppStatus();
                toSend.add("Oh, you want to calculate damage for a Pokemon associated with the trainer " + trainer.name + ".")
                    .add("\nI will show you the Pokemon of this trainer.");
                for (int i = 0; i < trainer.getPokemon().length; i++)
                    toSend.add((i + 1) + ". " + trainer.getPokemon()[i]);
                if (trainer.pokemonData.size() == 1) {
                    d.setPokemon(0);
                    toSend.add("\nOh, there is only one Pokemon found.")
                        .add("This means you want to calculate damage for " + d.getPokemon().name)
                        .add("Choose the move for which you want to calculate the damage:");
                    for (int i = 1; i <= d.getPokemon().moves.size(); i++)
                        toSend.add(i + ". " + d.getPokemon().moves.get(i));
                    toSend.add((d.getPokemon().moves.size() + 1) + ". " + d.getPokemon().syncMove.name + " (Sync Move)")
                        .add("Give your choice in an integer number in the range of 1-" + (d.getPokemon().moves.size() + 1))
                        .add("using the command `" + Bot.PREFIX + "choose <choice>`");
                    d.updateAppStatus();
                } else
                    toSend.add("\nGive your choice in an integer number in the range of 1-" + trainer.pokemonData.size())
                        .add("using the command `" + PREFIX + "choose <choice>`");
            }
        } else
            toSend.add("You have already chosen the trainer for this Damage Calculation Application.");
        e.send(toSend.toString()).queue();
    }
}
