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

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class VerifyCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String to_send="";
        long aid=e.getAuthor().getIdLong();
        if(Bot.current.DATA.containsKey(aid))
        {
            if(!Bot.current.DATA.get(aid).isVerified())
            {
                Damage d = Bot.current.DATA.get(aid);
                if(d.getAppStatus()<1)
                    to_send="Trainer is Missing!";
                if(d.getAppStatus()<2)
                    to_send+="\nSync Pair is Missing!";
                if(d.getAppStatus()<3)
                    to_send+="\nMove is Missing!";
                if(d.getStats()[0][d.getMInfo()[2]]==0)
                    to_send+="\n``"+MiscUtil.returnSP(d.getUid())+"``'s "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Attack stat is Missing!";
                if(d.getStats()[1][d.getMInfo()[2]+2]==0)
                    to_send+="\nThe target's "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Defense stat is Missing!";
                if(to_send.equals("")) {
                    to_send="Your PM Damage Calculator Application is now **verified**!";
                    d.verify();
                }
                Bot.current.DATA.put(aid,d);
            } else
                to_send="This app is already verified!";
        } else
            to_send="PM Damage Calculation Application not found";
        MiscUtil.send(e,to_send,true);
    }
}