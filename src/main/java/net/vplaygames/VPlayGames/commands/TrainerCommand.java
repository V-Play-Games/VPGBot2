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

import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.database.*;
import static net.vplaygames.VPlayGames.db.userdatabase.*;
import static net.vplaygames.VPlayGames.util.Array.returnID;

public class TrainerCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),trnr,to_send;
        int tstr,user_pv=returnID(user_ids,e.getAuthor().getIdLong());
        if (msg.startsWith(prefix+"trainer ")&&!e.getAuthor().isBot())
        {
            if (isRegistered(e.getAuthor().getIdLong()))
            {
                if (app_stts[user_pv]==0)
                {
                    trnr=msg.substring(prefix.length()+8);
                    tstr=returnID(trnrs, trnr);
                    e.getChannel().sendMessage("Wait... Let me check, if there is any trainer with this name.\n").queue();
                    if (trnrs[tstr].equals("NA"))
                        to_send="I cannot find any trainer with that name. Maybe this trainer is not usable in the game yet.";
                    else
                    {
                        to_send="Oh, you want to calculate damage for sync pairs associated with the trainer "+trnrs[tstr]+".\n\nI will show you the sync pairs including this trainer.";
                        tids[user_pv] = (tstr==0)?1:tstr;
                        app_stts[user_pv] = 1;
                        to_send+=returnSPs(tids[user_pv]);
                        ucs[user_pv] = tdabs[tids[user_pv]-1];
                        int[] uc = ucs[user_pv];
                        e.getChannel().sendMessage(to_send).queue();
                        if (uc.length==1)
                        {
                            int i;
                            tstr=0;
                            uids[user_pv]=uc[tstr];
                            pids[user_pv]=uc[tstr]%1000000;
                            to_send="Oh, there is only one sync pair found."+
                                    "\nThis means you want to calculate damage for "+returnSP(uc[tstr])+
                                    "\nChoose the move for which you want to calculate the damage:";
                            mSets[user_pv]=msets[pids[user_pv]%1000-1];
                            for (i=1; i<=mSets[user_pv].length; i++)
                                to_send+="\n"+i+". "+moves[(mSets[user_pv][i-1]-1)%1000];
                            to_send+="\n"+i+". "+smoves[pids[user_pv]%1000-1]+" (Sync Move)\nGive your choice in an integer number in the range of 1-"+(mSets[user_pv].length+1)+"\nusing the command ``"+prefix+"choose <choice>``";
                            app_stts[user_pv] = 2;
                        } else
                            to_send= "\nGive your choice in an integer number in the range of 1-"+uc.length+"\nusing the command ``"+prefix+"choose <choice>``";
                    }
                } else
                    to_send="You have already chosen the trainer for this Damage Calculation Application.";
            } else
                to_send="To use this command, you have to open up a new Damage Calculation Application.";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
    public static String returnSPs(int tid)
    {
        StringBuilder to_return = new StringBuilder();
        for (int i=0; i<tdabs[tid-1].length; i++)
            to_return.append("\n").append(i + 1).append(". ").append(returnSP(tdabs[tid - 1][i]));
        return to_return.toString();
    }
    public static String returnSP(int uid)
    {
        String sp=" and "+pkmns[uid%1000-1];
        String trnr=trnrs[uid/1000%1000];
        switch (uid/10000000)
        {
            case 1:
                trnr="Sygna Suit "+trnr;
                break;
            case 2:
                trnr+="(Holiday 20"+(19+(uid/1000000)%10)+")";
                break;
            case 3:
                trnr+="(Summer 20"+(19+(uid/1000000)%10)+")";
        }
        return trnr+sp;
    }
}