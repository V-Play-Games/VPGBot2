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
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class CalculateDamageCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send;
        long aid=e.getAuthor().getIdLong();
        if(!e.getAuthor().isBot()&&msg.equals(prefix+"cd"))
        {
            if(data.containsKey(aid))
            {
                System.out.println(data.get(aid).getVrfy());
                if(data.get(aid).isVerified())
                {
                    Damage d = data.get(aid);
                    String wthr=((d.getWthr()[0]==1)?"the weather was sunny":((d.getWthr()[1]==1)?"it was raining":((d.getWthr()[2]==1)?"there was a sandstorm":((d.getWthr()[3]==1)?"it was hailing":"the weather was normal"))));
                    String trgt_msg = (d.getMod()[2]==1)?"the target was the only opponent in the field":"there "+((d.getMod()[2]==2)?"was 1 more opponent":"were 2 more opponents")+" on the field";
                    int off = d.getStats()[0][d.getMInfo()[2]];
                    int def = d.getStats()[1][d.getMInfo()[2]+2];
                    int bp = d.getMInfo()[0];
                    double smb = 1+(d.getSml()-1)*5/100.0;
                    double roll = round(Math.random()*10+90)/100.0;
                    double ch = ((d.getMod()[0]==1)?1.5:1);
                    double se = ((d.getMod()[1]==1)?2:1);
                    double wthr_boost = (d.getWthr()[0]==1&&d.getMInfo()[3]==7)?1.5:(d.getWthr()[1]==1&&d.getMInfo()[3]==18)?1.5:1;
                    double sprd = (d.getMod()[2]==1)?1:(d.getMod()[2]==2)?0.66:0.5;
                    double mod = ch*se*wthr_boost*sprd;
                    long dmg = round(floor(floor(bp*smb)*off/def*mod*roll));
                    to_send="Formula:- (int: (int: "+bp+"x"+smb+")x("+off+"/"+def+")x("+ch+"x"+se+"x"+wthr_boost+"x"+sprd+")x"+roll+");"+
                            "\n\""+returnSP(d.getUid())+"\" with "+off+" "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Attack stat, while using "+d.getMvnam()+" at sync move level "+d.getSml()+", can deal "+dmg+" damage to an opponent with "+def+" "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Defense stat provided that "+trgt_msg+", "+wthr+", the hit was "+((d.getMod()[0]==1)?"not ":"")+"a critical hit and was "+((d.getMInfo()[0]==0)?"":"super ")+"effective against the opponent.";
                } else
                    to_send="Verify yourself using\""+prefix+"verify\" command first!";
            } else
                to_send="Start a PM Damage Calculation App first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}