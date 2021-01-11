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
import net.vplaygames.VPlayGames.processors.EventManager;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;

public class Response {
    private static HashMap<Long, Response> responses = new HashMap<>();
    private static HashMap<Long, MessageEmbed> exceptions = new HashMap<>();
    private final long INPUT_ID, INPUT_TIME;
    private final String LOCATION;
    private final StringBuilder OUTPUT;
    private final Message MESSAGE;
    private long outputTime;

    public Response(Message msg) {
        this(Bot.current.messagesProcessed, EventManager.currentTime, msg);
    }

    public Response(long id, long time, Message msg) {
        this.INPUT_ID = id;
        this.INPUT_TIME = time;
        this.LOCATION = Bot.current.LOCATION;
        this.MESSAGE = msg;
        this.OUTPUT = new StringBuilder();
        responses.put(id, this);
    }

    public Response Responded(String response) {
        OUTPUT.append(OUTPUT.toString().equals("") ? "" : "\n\\n\n").append(response);
        responses.put(INPUT_ID, this);
        return this;
    }

    public MessageEmbed getAsEmbed(boolean requested) {
        if (!requested) outputTime = System.currentTimeMillis();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setTitle("vpg.responses." + LOCATION + ".PROCESS_ID_" + INPUT_ID);
        emb.setDescription("Sent by: <@" + MESSAGE.getAuthor().getId() + "> in <#" + MESSAGE.getChannel().getIdLong() + ">");
        emb.setColor(0xffff33);
        emb.addField("Executed in " + (outputTime - INPUT_TIME) + " ms.", "", false);
        emb.addField("MESSAGE:", MESSAGE.getContentRaw(), false);
        emb.addField("RESPONSE:", OUTPUT.toString(), false);
        emb.setFooter("Log " + (requested ? "Requested" : "Sent") + " " + MiscUtil.dateTimeNow());
        return emb.build();
    }

    public static Response get(long set) {
        return responses.get(set);
    }

    public static MessageEmbed addExceptionLog(MessageEmbed exc) {
        return exceptions.put((Bot.current.lastExceptionID++), exc);
    }

    public static MessageEmbed getExceptionLog(long id) {
        return exceptions.get(id);
    }
}