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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.SkillGroup;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class SkillCommand extends DamageAppCommand {
    public SkillCommand() {
        super("skill", Damage.Status.UNIT_CHOSEN, 1, 0);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String skill = e.getMessage().getContentRaw().substring(e.getMessage().getContentRaw().split(" ")[1].length()+1);
        String toSend = "Wait... Let me check, if there is any skill with this name.";
        MiscUtil.send(e, toSend, true);
        if (SkillGroup.isSkill(skill))
            toSend = "I cannot find any skill with that name. Maybe this skill isn't added in the bot yet.";
        else {
            DATA.get(e.getAuthor().getIdLong()).addSkill(skill);
            toSend = "Successfully added " + Strings.toProperCase(skill) + " as a skill in your damage app.";
        }
        MiscUtil.send(e, toSend, true);
    }
}
