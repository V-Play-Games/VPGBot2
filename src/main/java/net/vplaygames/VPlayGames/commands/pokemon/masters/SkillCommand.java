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
import net.vplaygames.VPlayGames.core.PassiveSkill;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class SkillCommand extends DamageAppCommand {
    public SkillCommand() {
        super("skill", Damage.AppStatus.UNIT_CHOSEN, 1, 0);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        String arg = e.getArgsFrom(1).toString();
        PassiveSkill skill = PassiveSkill.get(arg);
        if (skill == null)
            toSend = "I cannot find any skill with that name. Maybe this skill isn't added in the bot yet.";
        else {
            int intensity = Strings.toInt(arg);
            DATA.get(e.getAuthor().getIdLong()).addSkill(skill, intensity);
            toSend = "Successfully added " + skill.name + (skill.intensive ? " " + intensity : "") + " as a skill in your damage app.";
        }
        e.send(toSend).queue();
    }
}
