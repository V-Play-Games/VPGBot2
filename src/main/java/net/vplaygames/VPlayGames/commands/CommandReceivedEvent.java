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
    long id, time;
    String content;
    String output;
    List<String> args;
    ICommand command;
    JSONObject logRepresentation;

    public CommandReceivedEvent(GuildMessageReceivedEvent e, String[] args, ICommand command) {
        this(e.getJDA(), e.getResponseNumber(), e.getMessage(), args, command);
    }

    public CommandReceivedEvent(JDA jda, long responseNumber, Message message, String[] args, ICommand command) {
        super(jda, responseNumber, message);
        this.content = message.getContentRaw();
        this.args = Arrays.asList(args);
        this.command = command;
        this.id = Bot.lastMessageProcessedId.getAndIncrement();
        this.time = System.currentTimeMillis();
        this.output = "";
        this.logRepresentation = new JSONObject();
        Bot.responses.put(id, logRepresentation);
        command.run(this);
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getArgsFrom(int index) {
        return args.subList(index, args.size());
    }

    public String getArg(int index) {
        return args.get(index);
    }

    public String getContent() {
        return content;
    }

    public String getOutput() {
        return output;
    }

    public ICommand getCommand() {
        return command;
    }

    public JSONObject getLogRepresentation() {
        return logRepresentation;
    }

    public void responded(String response) {
        output += response + "\n";
    }

    @CheckReturnValue
    public MessageAction send(String content) {
        responded(content);
        return getChannel().sendMessage(content);
    }

    public MessageAction send(MessageEmbed embed, String placeholder) {
        responded(placeholder);
        return getChannel().sendMessage(embed);
    }

    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    public void log() {
        logRepresentation.put("id", id);
        logRepresentation.put("time", time);
        logRepresentation.put("content", content);
        logRepresentation.put("output", output);
        logRepresentation.put("args", args);
        logRepresentation.put("command", command);
        logRepresentation.put("userId", getAuthor().getIdLong());
        logRepresentation.put("channelId", getChannel().getIdLong());
        logRepresentation.put("channelName", getChannel().getName());
        logRepresentation.put("messageId", getMessageIdLong());
        if (isFromGuild()) {
            logRepresentation.put("guildId", getGuild().getIdLong());
            logRepresentation.put("guildName", getGuild().getName());
        }
        if (Bot.logChannel != null) {
            File logRepresentationFile = MiscUtil.makeFileOf(logRepresentation, "log-file-" + id);
            Bot.logChannel.sendMessage(new EmbedBuilder()
                .setTitle("Process id " + id)
                .addField("Input", content.length() > 1024 ? content.substring(0, 1021) + "..." : content, false)
                .addField("Output", output.length() > 1024 ? output.substring(0, 1021) + "..." : output, false)
                .setFooter("Used in " + (isFromGuild() ? "the DM of " : "#" + getChannel().getName() + "(<#" + getChannel().getId() + ">) by ") + getAuthor().getAsTag() + " (" + getAuthor().getAsMention() + ")")
                .build())
                .addFile(logRepresentationFile)
                .queue(m -> logRepresentationFile.delete());
        }
    }
}
