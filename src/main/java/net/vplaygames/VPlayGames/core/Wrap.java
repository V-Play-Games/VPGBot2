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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.apache.commons.collections4.Bag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.List;

public class Wrap implements Message {
    private final Message m;
    private String content;

    public Wrap(Message m) {
        this(m, m.getContentRaw());
    }

    public Wrap(Message m, String content) {
        this.m = m;
        this.content = content;
    }

    @Nullable
    @Override
    public Message getReferencedMessage() {
        return m.getReferencedMessage();
    }

    @Nonnull
    @Override
    public List<User> getMentionedUsers() {
        return m.getMentionedUsers();
    }

    @Nonnull
    @Override
    public Bag<User> getMentionedUsersBag() {
        return m.getMentionedUsersBag();
    }

    @Nonnull
    @Override
    public List<TextChannel> getMentionedChannels() {
        return m.getMentionedChannels();
    }

    @NotNull
    @Override
    public Bag<TextChannel> getMentionedChannelsBag() {
        return m.getMentionedChannelsBag();
    }

    @Nonnull
    @Override
    public List<Role> getMentionedRoles() {
        return m.getMentionedRoles();
    }

    @Nonnull
    @Override
    public Bag<Role> getMentionedRolesBag() {
        return m.getMentionedRolesBag();
    }

    @Nonnull
    @Override
    public List<Member> getMentionedMembers(@Nonnull Guild guild) {
        return m.getMentionedMembers(guild);
    }

    @Nonnull
    @Override
    public List<Member> getMentionedMembers() {
        return m.getMentionedMembers();
    }

    @Nonnull
    @Override
    public List<IMentionable> getMentions(@Nonnull MentionType... types) {
        return m.getMentions(types);
    }

    @Override
    public boolean isMentioned(@Nonnull IMentionable mentionable, @Nonnull MentionType... types) {
        return m.isMentioned(mentionable, types);
    }

    @Override
    public boolean mentionsEveryone() {
        return m.mentionsEveryone();
    }

    @Override
    public boolean isEdited() {
        return m.isEdited();
    }

    @Nullable
    @Override
    public OffsetDateTime getTimeEdited() {
        return m.getTimeEdited();
    }

    @Nonnull
    @Override
    public User getAuthor() {
        return m.getAuthor();
    }

    @Nullable
    @Override
    public Member getMember() {
        return m.getMember();
    }

    @Nonnull
    @Override
    public String getJumpUrl() {
        return m.getJumpUrl();
    }

    @Nonnull
    @Override
    public String getContentDisplay() {
        return content;
    }

    @Nonnull
    @Override
    public String getContentRaw() {
        return content;
    }

    @Nonnull
    @Override
    public String getContentStripped() {
        return m.getContentStripped();
    }

    @Nonnull
    @Override
    public List<String> getInvites() {
        return m.getInvites();
    }

    @Nullable
    @Override
    public String getNonce() {
        return m.getNonce();
    }

    @Override
    public boolean isFromType(@Nonnull ChannelType type) {
        return m.isFromType(type);
    }

    @Override
    public boolean isFromGuild() {
        return m.isFromGuild();
    }

    @Nonnull
    @Override
    public ChannelType getChannelType() {
        return m.getChannelType();
    }

    @Override
    public boolean isWebhookMessage() {
        return m.isWebhookMessage();
    }

    @Nonnull
    @Override
    public MessageChannel getChannel() {
        return m.getChannel();
    }

    @Nonnull
    @Override
    public PrivateChannel getPrivateChannel() {
        return m.getPrivateChannel();
    }

    @Nonnull
    @Override
    public TextChannel getTextChannel() {
        return m.getTextChannel();
    }

    @Nullable
    @Override
    public Category getCategory() {
        return m.getCategory();
    }

    @Nonnull
    @Override
    public Guild getGuild() {
        return m.getGuild();
    }

    @Nonnull
    @Override
    public List<Attachment> getAttachments() {
        return m.getAttachments();
    }

    @Nonnull
    @Override
    public List<MessageEmbed> getEmbeds() {
        return m.getEmbeds();
    }

    @Nonnull
    @Override
    public List<Emote> getEmotes() {
        return m.getEmotes();
    }

    @Nonnull
    @Override
    public Bag<Emote> getEmotesBag() {
        return m.getEmotesBag();
    }

    @Nonnull
    @Override
    public List<MessageReaction> getReactions() {
        return m.getReactions();
    }

    @Override
    public boolean isTTS() {
        return m.isTTS();
    }

    @Nullable
    @Override
    public MessageActivity getActivity() {
        return m.getActivity();
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull CharSequence newContent) {
        return m.editMessage(newContent);
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull MessageEmbed newContent) {
        return m.editMessage(newContent);
    }

    @Nonnull
    @Override
    public MessageAction editMessageFormat(@Nonnull String format, @Nonnull Object... args) {
        return m.editMessageFormat(format, args);
    }

    @Nonnull
    @Override
    public MessageAction editMessage(@Nonnull Message newContent) {
        return m.editMessage(newContent);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull CharSequence content) {
        return m.reply(content);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull MessageEmbed content) {
        return m.reply(content);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull Message content) {
        return m.reply(content);
    }

    @Nonnull
    @Override
    public MessageAction replyFormat(@Nonnull String format, @Nonnull Object... args) {
        return m.replyFormat(format, args);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull File file, @Nonnull AttachmentOption... options) {
        return m.reply(file, options);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull File data, @Nonnull String name, @Nonnull AttachmentOption... options) {
        return m.reply(data,name,options);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull InputStream data, @Nonnull String name, @Nonnull AttachmentOption... options) {
        return m.reply(data, name, options);
    }

    @Nonnull
    @Override
    public MessageAction reply(@Nonnull byte[] data, @Nonnull String name, @Nonnull AttachmentOption... options) {
        return m.reply(data, name, options);
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> delete() {
        return m.delete();
    }

    @Nonnull
    @Override
    public JDA getJDA() {
        return m.getJDA();
    }

    @Override
    public boolean isPinned() {
        return m.isPinned();
    }

    @Nonnull
    @Override
    public RestAction<Void> pin() {
        return m.pin();
    }

    @Nonnull
    @Override
    public RestAction<Void> unpin() {
        return m.unpin();
    }

    @Nonnull
    @Override
    public RestAction<Void> addReaction(@Nonnull Emote emote) {
        return m.addReaction(emote);
    }

    @Nonnull
    @Override
    public RestAction<Void> addReaction(@Nonnull String unicode) {
        return m.addReaction(unicode);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions() {
        return m.clearReactions();
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions(@Nonnull String unicode) {
        return m.clearReactions(unicode);
    }

    @Nonnull
    @Override
    public RestAction<Void> clearReactions(@Nonnull Emote emote) {
        return m.clearReactions(emote);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull Emote emote) {
        return m.removeReaction(emote);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull Emote emote, @Nonnull User user) {
        return m.removeReaction(emote, user);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull String unicode) {
        return m.removeReaction(unicode);
    }

    @Nonnull
    @Override
    public RestAction<Void> removeReaction(@Nonnull String unicode, @Nonnull User user) {
        return m.removeReaction(unicode, user);
    }

    @Nonnull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@Nonnull Emote emote) {
        return m.retrieveReactionUsers(emote);
    }

    @Nonnull
    @Override
    public ReactionPaginationAction retrieveReactionUsers(@Nonnull String unicode) {
        return m.retrieveReactionUsers(unicode);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionByUnicode(@Nonnull String unicode) {
        return m.getReactionByUnicode(unicode);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(@Nonnull String id) {
        return m.getReactionById(id);
    }

    @Nullable
    @Override
    public MessageReaction.ReactionEmote getReactionById(long id) {
        return null;
    }

    @Nonnull
    @Override
    public AuditableRestAction<Void> suppressEmbeds(boolean suppressed) {
        return m.suppressEmbeds(suppressed);
    }

    @Nonnull
    @Override
    public RestAction<Message> crosspost() {
        return m.crosspost();
    }

    @Override
    public boolean isSuppressedEmbeds() {
        return m.isSuppressedEmbeds();
    }

    @Nonnull
    @Override
    public EnumSet<MessageFlag> getFlags() {
        return m.getFlags();
    }

    @Nonnull
    @Override
    public MessageType getType() {
        return m.getType();
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        m.formatTo(formatter, flags, width, precision);
    }

    @Nonnull
    @Override
    public String getId() {
        return m.getId();
    }

    @Override
    public long getIdLong() {
        return m.getIdLong();
    }

    @Nonnull
    @Override
    public OffsetDateTime getTimeCreated() {
        return m.getTimeCreated();
    }
}
