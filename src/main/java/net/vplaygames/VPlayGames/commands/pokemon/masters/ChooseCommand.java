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

import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.DamageAppCommand;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.StringJoiner;

public class ChooseCommand extends DamageAppCommand {
    public ChooseCommand() {
        super("choose", 1, "c");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        StringJoiner toSend = new StringJoiner("\n");
        Damage d = Bot.DATA.get(e.getAuthor().getIdLong());
        int temp = Strings.toInt(e.getArg(1)) - 1;
        if (d.getAppStatus() == 1) {
            if (temp < 0 || temp >= d.getTrainer().pokemonData.size())
                toSend.add("Invalid Input. There is no trainer at that place.");
            else {
                d.setPokemon(temp);
                toSend.add("This means you want to calculate damage for " + d.getPokemon().name)
                    .add("Choose the move for which you want to calculate the damage:");
                for (int i = 0; i < d.getPokemon().moves.size(); i++)
                    toSend.add((i+1) + ". " + d.getPokemon().moves.get(i).name);
                toSend.add((d.getPokemon().moves.size()+1) + ". " + d.getPokemon().syncMove.name + " (Sync Move)")
                    .add("Give your choice in an integer number in the range of 1-" + (d.getPokemon().moves.size() + 1))
                    .add("using the command `" + Bot.PREFIX + "choose <choice>`");
                d.updateAppStatus();
            }
        } else if (d.getAppStatus() == 2) {
            if (temp < 0 || temp > d.getPokemon().moves.size())
                toSend.add("Invalid Input. There is no move at that place.");
            else {
                d.setAttack(temp);
                if (d.getAttack().minPower==0) {
                    toSend.add("Choose a damaging attack! \""+d.getAttack().name +"\" is not a damaging attack.");
                } else {
                    toSend.add("You chose " + d.getAttack().name + ".")
                        .add("\nMove Info:-")
                        .add("Base Power: " + d.getAttack().minPower)
                        .add("Category: " + d.getAttack().category)
                        .add("Target: " + d.getAttack().target)
                        .add("Type: " + d.getAttack().type)
                        .add("\nUse `" + Bot.PREFIX + "view move info` to view this info again.");
                    if (d.getAttack().target.equals("All Opponents")) {
                        toSend.add("This move can affect more than 1 targets.")
                            .add("How many targets were on the field when the move was used?")
                            .add("Give your choice in an integer number in the range of 1-3")
                            .add("using the command `" + Bot.PREFIX + "choose <choice>`");
                    } else
                        d.updateAppStatus();
                    d.updateAppStatus();
                }
            }
        } else if (d.getAppStatus() == 3) {
            if (temp < 0 || temp > 2)
                toSend.add("Invalid no. of targets.");
            else {
                d.setMod(2, temp+1).updateAppStatus();
                toSend.add("Set the no. of targets to " + temp + ".");
            }
        } else
            toSend.add("Cannot find a list to choose from.");
        e.send(toSend.toString()).queue();
    }
}
