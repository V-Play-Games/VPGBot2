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
package net.vplaygames.VPlayGames.commands.botStaff;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.List;

public class SyncCommand extends BotStaffCommand {
    public SyncCommand() {
        super("sync");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String s;
        if(msg[1].equals("in"))
        {
            List<Message.Attachment> attachments = e.getMessage().getAttachments();
            attachments.get(0).downloadToFile();
            Bot.syncData(Bot.damageData);
            s="Data Synced In!";
        } else
            s="Wrong syntax!\nSyntax for "+Bot.PREFIX+"sync command: ``"+Bot.PREFIX+"sync in``";
        MiscUtil.send(e,s,true);
    }
}
