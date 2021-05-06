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
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.Passive;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class SkillCommand extends DamageAppCommand {
    public SkillCommand() {
        super("skill", Damage.AppStatus.UNIT_CHOSEN, 1, 0);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        Passive passive = Passive.of(String.join("", e.getArgsFrom(1)));
        if (passive.skill == null)
            toSend = "I cannot find any skill with that name. Maybe this skill isn't added in the bot yet.";
        else if (passive.intensity == 0 && passive.skill.intensive)
            toSend = "There must be a number after the name of the Passive Skill.\nFor Example: Power Flux **3**";
        else {
            DATA.get(e.getAuthor().getIdLong()).addSkill(passive);
            toSend = "Successfully added " + passive + " as a skill in your damage app.";
        }
        e.send(toSend).queue();
    }
}
