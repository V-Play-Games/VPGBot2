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

import net.vplaygames.VPlayGames.db.Damage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.botresources.prefix;

public class VerifyCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send="";
        long aid=e.getAuthor().getIdLong();
        if(!e.getAuthor().isBot()&&msg.equals(prefix+"verify"))
        {
            if(data.containsKey(aid))
            {
                if(!data.get(aid).isVerified())
                {
                    Damage d = data.get(aid);
                    if(d.getApp_stts()<1)
                        to_send="Trainer is Missing!";
                    if(d.getApp_stts()<2)
                        to_send+="\nSync Pair is Missing!";
                    if(d.getApp_stts()<3)
                        to_send+="\nMove is Missing!";
                    if(d.getSml()==0)
                        to_send+="\nSync Move Level is Missing!";
                    if(d.getStats()[0][d.getMInfo()[2]]==0)
                        to_send+="\n``"+returnSP(d.getUid())+"``'s "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Attack stat is Missing!";
                    if(d.getStats()[1][d.getMInfo()[2]+2]==0)
                        to_send+="\nThe target's "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Defense stat is Missing!";
                    if(to_send.equals("")) {
                        to_send="Your PM Damage Calculator Application is now **verified**!";
                        d.verify();
                    }
                    data.put(aid,d);
                } else
                    to_send="This app is already verified!";
            } else
                to_send="PM Damage Calculation Application not found";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
