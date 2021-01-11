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
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class CalculateDamageCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send;
        int user_pv = Array.returnID(user_ids,e.getAuthor().getIdLong());
        if(msg.equals(prefix+"cd"))
        {
            if(isRegistered(e.getAuthor().getIdLong()))
            {
                if(vrfy[user_pv]==1)
                {
                    String wthr=((wthrs[user_pv][0]==1)?"the weather was sunny":((wthrs[user_pv][1]==1)?"it was raining":((wthrs[user_pv][2]==1)?"there was a sandstorm":((wthrs[user_pv][3]==1)?"it was hailing":"the weather was normal"))));
                    int off = stats_u[user_pv][mInfos[user_pv][2]];
                    int def = stats_t[user_pv][mInfos[user_pv][2]+2];
                    int bp = mInfos[user_pv][0];
                    double smb = 1+(smls[user_pv]-1)*5/100.0;
                    double roll = Math.rint(Math.random()*10+90)/100.0;
                    double ch = ((mods[user_pv][0]==1)?1.5:1);
                    double se = ((mods[user_pv][1]==1)?2:1);
                    double wthr_boost = (wthrs[user_pv][0]==1&&mInfos[user_pv][3]==7)?1.5:(wthrs[user_pv][1]==1&&mInfos[user_pv][3]==18)?1.5:1;
                    double mod = ch*se*wthr_boost;
                    long dmg = round(floor(floor(bp*smb)*off/def*mod*roll));
                    to_send="Formula:- (int: (int: "+bp+"x"+smb+")x("+off+"/"+def+")x("+ch+"x"+se+"x"+wthr_boost+")x"+roll+");"+
                            "\n\""+returnSP(uids[user_pv])+"\" with "+off+" "+((mInfos[user_pv][2]==1)?"Special":"Physical")+" Attack stat, while using "+mvnams[user_pv]+" at sync move level "+smls[user_pv]+", can deal "+dmg+" damage to an opponent with "+def+" "+((mInfos[user_pv][2]==1)?"Special":"Physical")+" Defense stat provided that "+wthr+", the hit was "+((mods[user_pv][0]==0)?"not ":"")+"a critical hit and was "+((mods[user_pv][0]==0)?"":"super ")+"effective against the opponent.";
                } else
                    to_send="Verify yourself using\""+prefix+"verify\" command first!";
            } else
                to_send="Start a PM Damage Calculation App first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
