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
package net.vplaygames.VPlayGames.core;

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

public class Response {
    static HashMap<Long, Response> responses = new HashMap<>();
    static HashMap<Long, MessageEmbed> exceptions = new HashMap<>();
    public final long INPUT_ID, INPUT_TIME;
    public final StringBuilder OUTPUT;
    public final Message MESSAGE;
    public long outputTime;

    public Response(Message msg) {
        this(Bot.messagesProcessed, EventHandler.currentTime, msg);
    }

    public Response(long id, long time, Message msg) {
        this.INPUT_ID = id;
        this.INPUT_TIME = time;
        this.MESSAGE = msg;
        this.OUTPUT = new StringBuilder();
        responses.put(id, this);
    }

    public void responded(String response) {
        OUTPUT.append(response).append("\n");
        responses.put(INPUT_ID, this);
    }

    public MessageEmbed getAsEmbed(boolean requested) {
        if (!requested) outputTime = currentTimeMillis();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("ID [" + Bot.LOCATION + "]: " + INPUT_ID);
        emb.setDescription("Sent by: <@" + MESSAGE.getAuthor().getId() + "> in <#" + MESSAGE.getChannel().getIdLong() + ">");
        emb.setColor(0x1abc9c);
        emb.addField("Executed in " + (outputTime - INPUT_TIME) + " ms.", "", false);
        emb.addField("Message:", MESSAGE.getContentRaw(), false);
        emb.addField("Response:", OUTPUT.toString(), false);
        emb.setFooter("Log " + (requested ? "Requested" : "Sent") + " " + MiscUtil.dateTimeNow());
        return emb.build();
    }

    @Override
    public String toString() {
        return "Response ID [" + Bot.LOCATION + "]: " + INPUT_ID +
                "\nProcessing time: " + ((outputTime = currentTimeMillis()) - INPUT_TIME) + "ms" +
                "\nSent by: <@" + MESSAGE.getAuthor().getId() + " in the channel <#" + MESSAGE.getChannel().getIdLong() + "> " + MiscUtil.dateTimeNow() +
                "\nMessage: " + MESSAGE.getContentRaw() +
                "\nResponse: " + OUTPUT.toString();
    }

    public static Response get(long set) {
        return responses.get(set);
    }

    public static MessageEmbed addExceptionLog(MessageEmbed exc) {
        exceptions.put(Bot.lastExceptionID, exc);
        return exceptions.get(Bot.lastExceptionID++);
    }

    public static HashMap<Long, MessageEmbed> getExceptionLog() {
        return exceptions;
    }
}