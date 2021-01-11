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
package net.vplaygames.VPlayGames.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vplaygames.VPlayGames.util.Array;

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.userdatabase.*;

public class VerifyCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send="";
        int user_pv= Array.returnID(user_ids,e.getAuthor().getIdLong());
        if(msg.equals(prefix+"verify"))
        {
            if(isRegistered(e.getAuthor().getIdLong()))
            {
                if(app_stts[user_pv]<1)
                    to_send="Trainer is Missing!";
                if(app_stts[user_pv]<2)
                    to_send+="\nSync Pair is Missing!";
                if(app_stts[user_pv]<3)
                    to_send+="\nMove is Missing!";
                if(smls[user_pv]==0)
                    to_send+="\nSync Move Level is Missing!";
                if(stats_u[user_pv][mInfos[user_pv][2]]==0)
                    to_send+="\n``"+returnSP(uids[user_pv])+"``'s "+((mInfos[user_pv][2]==1)?"Special":"Physical")+" Attack stat is Missing!";
                if(stats_t[user_pv][mInfos[user_pv][2]+2]==0)
                    to_send+="\nThe target's "+((mInfos[user_pv][2]==1)?"Special":"Physical")+" Defense stat is Missing!";
                if(to_send.equals("")) {
                    to_send="Your PM Damage Calculator Application is now **verified**!";
                    vrfy[user_pv]=1;
                }
            } else
                to_send="PM Damage Calculation Application not found";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
