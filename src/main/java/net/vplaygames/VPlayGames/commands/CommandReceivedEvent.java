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
package net.vplaygames.VPlayGames.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import org.json.simple.JSONObject;

import javax.annotation.CheckReturnValue;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandReceivedEvent extends MessageReceivedEvent {
    public boolean forceNotLog;
    public long id, time;
    public String content;
    public String output = "";
    public List<String> args;
    public ICommand command;
    public JSONObject logRepresentation = new JSONObject();
    public Throwable trouble;

    public CommandReceivedEvent(GuildMessageReceivedEvent e, String[] args, ICommand command) {
        this(e.getJDA(), e.getResponseNumber(), e.getMessage(), args, command);
    }

    public CommandReceivedEvent(JDA jda, long responseNumber, Message message, String[] args, ICommand command) {
        super(jda, responseNumber, message);
        this.content = message.getContentRaw();
        this.args = Arrays.asList(args);
        this.command = command;
        this.id = Bot.lastCommandId.getAndIncrement();
        this.time = System.currentTimeMillis();
        Bot.responses.put(id, logRepresentation);
        command.run(this);
    }

    public List<String> getArgsFrom(int index) {
        return args.subList(index, args.size());
    }

    public String getArg(int index) {
        return args.get(index);
    }

    public void responded(String response) {
        output += response + "\n";
    }

    @CheckReturnValue
    public MessageAction send(String content) {
        responded(content);
        return getChannel().sendMessage(content).reference(getMessage()).mentionRepliedUser(false);
    }

    public MessageAction send(MessageEmbed embed, String placeholder) {
        responded(placeholder);
        return getChannel().sendMessage(embed).reference(getMessage()).mentionRepliedUser(false);
    }

    public void reportTrouble(Throwable t) {
        trouble = t;
        t.printStackTrace();
    }

    public void forceNotLog() {
        forceNotLog = true;
    }

    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    public void log() {
        logRepresentation.put("id", id);
        logRepresentation.put("time", time);
        logRepresentation.put("content", content);
        logRepresentation.put("output", output);
        logRepresentation.put("args", args);
        logRepresentation.put("command", command.toString());
        logRepresentation.put("userId", getAuthor().getIdLong());
        logRepresentation.put("channelId", getChannel().getIdLong());
        logRepresentation.put("channelName", getChannel().getName());
        logRepresentation.put("messageId", getMessageIdLong());
        logRepresentation.put("trouble", trouble);
        if (isFromGuild()) {
            logRepresentation.put("guildId", getGuild().getIdLong());
            logRepresentation.put("guildName", getGuild().getName());
        }
        if (!forceNotLog) {
            File logRepresentationFile = MiscUtil.makeFileOf(logRepresentation, "log-file-" + id);
            Bot.logChannel.sendMessage(new EmbedBuilder()
                .setTitle("Process id " + id)
                .setDescription("Error: " + (trouble == null
                    ? "None"
                    : trouble.getClass() + ": " + trouble.getMessage() + "\n\t at " + trouble.getStackTrace()[0])+
                    "\nUsed in " + (!isFromGuild() ? "the DM of " : "#" + getChannel().getName() + "(<#" + getChannel().getId() + ">) by ")
                    + getAuthor().getAsTag() + " (" + getAuthor().getAsMention() + ")")
                .addField("Input", content.length() > 1024 ? content.substring(0, 1021) + "..." : content, false)
                .addField("Output", output.length() > 1024 ? output.substring(0, 1021) + "..." : output, false)
                .build())
                .addFile(logRepresentationFile)
                .queue(m -> logRepresentationFile.delete());
        }
    }
}
