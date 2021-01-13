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

import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;
import static java.lang.Math.floor;
import static java.lang.Math.round;

public class CDCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String toSend;
        long aid=e.getAuthor().getIdLong();
        if(Bot.current.DATA.containsKey(aid))
        {
            if(Bot.current.DATA.get(aid).isVerified())
            {
                Damage d = Bot.current.DATA.get(aid);
                int off = d.getStats()[0][d.getMInfo()[2]];
                int def = d.getStats()[1][d.getMInfo()[2]+2];
                int bp = d.getMInfo()[0];
                int b_o = Math.abs(d.getBuffs()[0][d.getMInfo()[2]]);
                double buff_off = (b_o==0)?1:(b_o==1)?1.25:(10+b_o+2)/10.0;
                buff_off = (d.getBuffs()[0][d.getMInfo()[2]]<0)?1.0/buff_off:buff_off;
                int b_d = Math.abs(d.getBuffs()[1][d.getMInfo()[2]+2]);
                double buff_def = (b_d==0)?1:(b_d==1)?1.25:(10+b_d+2)/10.0;
                buff_def = (d.getBuffs()[1][d.getMInfo()[2]+2]<0)?1.0/buff_def:(d.getMod()[0]==1)?1:buff_def;
                double smb = 1+(d.getSml()-1)/20.0;
                double roll = round(Math.random()*10+90)/100.0;
                double ch = (d.getMod()[0]==1)?1.5:1;
                double se = (d.getMod()[1]==1)?2:1;
                double wthr_boost = (d.getWthr()[0]==1&&d.getMInfo()[3]==7)?1.5:(d.getWthr()[1]==1&&d.getMInfo()[3]==18)?1.5:1;
                double sprd = (d.getMod()[2]==1)?1:(d.getMod()[2]==2)?2.0/3.0:0.5;
                double mod = ch*se*wthr_boost*sprd;
                long dmg = round(floor(floor(bp*smb*(1+d.getPassiveMultiplier()))*(off*buff_off)/(def*buff_def)*mod*roll));
                toSend="Formula:- (int: (int: "+bp+"x"+smb+"x(1"+d.getMultiplierString()+") ) x ( (int: "+off+"x"+buff_off+") / (int: "+def+"x"+buff_def+" ) ) x ( "+ch+"x"+se+"x"+wthr_boost+"x"+sprd+" ) x "+roll+" );"+
                        "\n\""+returnSP(d.getUid())+"\" with "+
                        off+" "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Attack stat, while using "+
                        d.getMoveName()+" at sync move level "+d.getSml()+
                        ", can deal **__"+dmg+"__** damage to an opponent with "+
                        def+" "+((d.getMInfo()[2]==1)?"Special":"Physical")+" Defense stat provided that "+
                        d.getTargetString()+", "+d.getWeatherString()+
                        ", the hit was "+((ch==1)?"not ":"")+"a critical hit and was "+
                        ((se==1)?"":"super ")+"effective against the opponent.\n\n"+d.getPassiveString();
            } else
                toSend="Verify the damage app using ``"+Bot.current.PREFIX+"verify`` command first!";
        } else
            toSend="Start a PM Damage Calculation App first!";
        MiscUtil.send(e,toSend,true);
    }
}