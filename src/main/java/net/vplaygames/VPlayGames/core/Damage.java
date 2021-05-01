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
package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.entities.Move;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.SyncMove;
import com.vplaygames.PM4J.entities.Trainer;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.Math.floor;
import static java.lang.Math.round;
import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class Damage {
    boolean enabled;
    int sml;
    int gauge;
    long damage;
    long userId;
    int[] mod;
    int[] hp;
    int[][] stats;
    int[][] buffs;
    int[][] interference;
    String damageCode;
    String damageString;
    AppStatus appStatus;
    Trainer trainer;
    Pokemon pokemon;
    Attack attack;
    Weather weather;
    Terrain terrain;
    Status enemyStatus;
    Status userStatus;
    ArrayList<SkillGroup> skills;

    public Damage(long userId) {
        this(userId, generateValid());
    }

    public Damage(long uId, String code) {
        enabled = false;
        userId = uId;
        sml = 1;
        appStatus = AppStatus.of(0);
        gauge = 0;
        hp = new int[]{0, 0};
        mod = new int[]{0, 0, 0, 0};
        weather = Weather.NORMAL;
        terrain = Terrain.NORMAL;
        enemyStatus = userStatus = Status.NORMAL;
        interference = new int[][]{{0, 0, 0}, {0, 0, 0}};
        stats = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}};
        buffs = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        mod[2] = 1;
        damageCode = code;
        skills = new ArrayList<>();
        damage = 0;
        damageString = "0";
        Bot.DATA.put(userId, this);
    }

    public static String generateValid() {
        StringBuilder tor = new StringBuilder();
        do {
            tor.delete(0, 6);
            for (int i = 0; i < 6; i++)
                tor.append(Bot.BASE64.charAt((int) (Math.random() * 64)));
        } while (Bot.DAMAGE_CODES.containsKey(tor.toString()));
        return tor.toString();
    }

    // Remover
    public void remove() {
        Bot.DAMAGE_CODES.remove(damageCode);
        DATA.remove(userId);
    }

    // Setters
    public Damage copyFrom(Damage d) {
        trainer = d.trainer;
        pokemon = d.pokemon;
        attack = d.attack;
        sml = d.sml;
        gauge = d.gauge;
        mod = d.mod;
        weather = d.weather;
        terrain = d.terrain;
        stats = d.stats;
        buffs = d.buffs;
        hp = d.hp;
        enemyStatus = d.enemyStatus;
        userStatus = d.userStatus;
        interference = d.interference;
        skills = new ArrayList<>(d.skills);
        enabled = d.enabled;
        appStatus = d.appStatus;
        damage = d.damage;
        damageString = d.damageString;
        return this;
    }

    public Damage enable() {
        enabled = true;
        Bot.DAMAGE_CODES.put(damageCode, this);
        return this;
    }

    public Damage setTrainer(Trainer set) {
        trainer = set;
        return this;
    }

    public Damage setPokemon(Pokemon set) {
        pokemon = set;
        return this;
    }

    public Damage setPokemon(int set) {
        pokemon = trainer.pokemonData.get(set);
        return this;
    }

    public Damage setAttack(Attack set) {
        attack = set;
        return this;
    }

    public Damage setAttack(Move set) {
        attack = new Attack(set);
        return this;
    }

    public Damage setAttack(SyncMove set) {
        attack = new Attack(set);
        return this;
    }

    public Damage setAttack(int set) {
        attack = pokemon.moves.size() == set ? new Attack(pokemon.syncMove) : new Attack(pokemon.moves.get(set));
        return this;
    }

    public Damage setSml(int set) {
        sml = set;
        return this;
    }

    public Damage incrementAppStatus() {
        appStatus = appStatus.next();
        return this;
    }

    public Damage setGauge(int set) {
        gauge = set;
        return this;
    }

    public Damage setMod(int i, int set) {
        mod[i] = set;
        return this;
    }

    public Damage setWeather(Weather weather) {
        this.weather = weather;
        return this;
    }

    public Damage setTerrain(Terrain terrain) {
        this.terrain = terrain;
        return this;
    }

    public Damage setStatus(boolean u, Status status) {
        if (u)
            userStatus = status;
        else
            enemyStatus = status;
        return this;
    }

    public Damage setInterference(int t, int set) {
        interference[t][set] = (interference[t][set] == 1) ? 0 : 1;
        return this;
    }

    public Damage setStats(int target, int sttcd, int stat) {
        stats[target][sttcd] = stat;
        return this;
    }

    public Damage setBuffs(int target, int bffcd, int buff) {
        buffs[target][bffcd] = buff;
        return this;
    }

    public Damage setHP(int target, int set) {
        hp[target] = set;

        return this;
    }

    public Damage addSkill(PassiveSkill skill, int intensity) {
        skills.add(new SkillGroup(skill, intensity));
        return this;
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public long getUserId() {
        return userId;
    }

    public int getAppStatusAsInt() {
        return appStatus.ordinal();
    }

    public AppStatus getAppStatus() {
        return appStatus;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Attack getAttack() {
        return attack;
    }

    public int getSml() {
        return sml;
    }

    public int getGauge() {
        return gauge;
    }

    public String getCode() {
        return damageCode;
    }

    public int[] getMod() {
        return mod;
    }

    public Weather getWeather() {
        return weather;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int[] getHP() {
        return hp;
    }

    public Status getStatus(boolean u) {
        return u ? userStatus : enemyStatus;
    }

    public int[][] getInterference() {
        return interference;
    }

    public int[][] getStats() {
        return stats;
    }

    public int[][] getBuffs() {
        return buffs;
    }

    public ArrayList<SkillGroup> getSkills() {
        return skills;
    }

    public void refresh() {
        int mCat = MiscUtil.isSpecial(attack.category);
        int mType = MiscUtil.returnTypeId(attack.type);
        int off = stats[0][mCat];
        int def = stats[1][mCat + 2];
        int bp = attack.minPower;
        int b_o = Math.abs(buffs[0][mCat]);
        double buff_off = (b_o == 0) ? 1 : (b_o == 1) ? 1.25 : (10 + b_o + 2) / 10.0;
        buff_off = (buffs[0][mCat] < 0) ? 1.0 / buff_off : buff_off;
        int b_d = Math.abs(buffs[1][mCat + 2]);
        double buff_def = (b_d == 0) ? 1 : (b_d == 1) ? 1.25 : (10 + b_d + 2) / 10.0;
        buff_def = (buffs[1][mCat + 2] < 0) ? 1.0 / buff_def : (mod[0] == 1) ? 1 : buff_def;
        double smb = 1 + (sml - 1) / 20.0;
        double roll = round(Math.random() * 10 + 90) / 100.0;
        double ch = (mod[0] == 1) ? 1.5 : 1;
        double se = (mod[1] == 1) ? 2 : 1;
        double spread = (mod[2] == 1) ? 1 : (mod[2] == 2) ? 2.0 / 3.0 : 0.5;
        double wthr_boost = (weather.equals(Weather.SUN) && mType == 7) || (weather.equals(Weather.RAIN) && mType == 18) ? 1.5 : 1;
        double terrain_boost = (terrain.equals(Terrain.ELECTRIC) && mType == 4) || (terrain.equals(Terrain.MISTY) && mType == 15) || (terrain.equals(Terrain.PSYCHIC) && mType == 10) || (terrain.equals(Terrain.GRASSY) && mType == 5) ? 1.5 : 1;
        double mod = ch * se * spread * wthr_boost * terrain_boost;
        damage = round(floor(floor(bp * smb * (1 + getPassiveMultiplier())) * (off * buff_off) / (def * buff_def) * mod * roll));
        damageString = "Formula:- (int: (int: " + bp + "x" + smb + "x(1" + getMultiplierString() + "))x((" + off + "x" + buff_off + ")/(" + def + "x" + buff_def + "))x(" + ch + "x" + se + "x" + wthr_boost + "x" + terrain_boost + "x" + spread + ")x" + roll + ")" +
            "\n\"" + trainer.name + "\" with " +
            off + " " + (mCat == 1 ? "Special" : "Physical") + " Attack stat, while using " +
            attack.name + " at sync move level " + sml +
            ", can deal **__" + damage + "__** damage to an opponent with " +
            def + " " + (mCat == 1 ? "Special" : "Physical") + " Defense stat provided that " +
            getTargetString() + ", " + weather + ", " + terrain +
            ", the hit was " + ((ch == 1) ? "not " : "") + "a critical hit and was " +
            ((se == 1) ? "" : "super ") + "effective against the opponent.\n\n" + getPassiveString();
    }

    // Other Getters
    public String getTargetString() {
        return (mod[2] == 1) ? "the target was the only opponent affected by the move" : "there " + ((mod[2] == 2) ? "was 1 more opponent" : "were 2 more opponents") + " other than the target affected by the move";
    }

    public String getPassiveString() {
        Stream<SkillGroup> stream = skills.stream().filter(sg -> sg.isActive(this));
        if (stream.count()==0) return "";
        StringJoiner tor = new StringJoiner("\n").add("Skills affecting the damage:-");
        AtomicInteger i = new AtomicInteger(1);
        stream.forEach(sg -> tor.add(i.getAndIncrement() + ". " + sg.getPassiveString()));
        return tor.toString();
    }

    public String getMultiplierString() {
        return skills.stream()
            .filter(sg -> sg.isActive(this))
            .collect(StringBuilder::new,
                (stringBuilder, skillGroup) -> stringBuilder.append(skillGroup.getMultiplierString()),
                StringBuilder::append)
            .toString();
    }

    public double getPassiveMultiplier() {
        return skills.stream().filter(sg -> sg.isActive(this)).mapToDouble(SkillGroup::getMultiplier).sum();
    }

    public long getDamage() {
        refresh();
        return damage;
    }

    public String getDamageString() {
        refresh();
        return damageString;
    }

    // Methods overridden from java.lang.Object
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Damage))
            return false;
        Damage d = (Damage) o;
        return trainer == d.trainer &&
            pokemon == d.pokemon &&
            attack == d.attack &&
            sml == d.sml &&
            appStatus == d.appStatus &&
            gauge == d.gauge &&
            mod == d.mod &&
            weather.equals(d.weather) &&
            stats == d.stats &&
            buffs == d.buffs &&
            enemyStatus == d.enemyStatus &&
            userStatus == d.userStatus &&
            interference == d.interference &&
            skills.equals(d.skills);
    }

    @Override
    public String toString() {
        return "Damage{" +
            "enabled=" + enabled +
            (trainer == null ? "" :", trainer=" +  trainer.name) +
            (pokemon == null ? "" :", pokemon=" +  pokemon.name) +
            (attack == null ? "" :", attack=" +  attack.name) +
            ", sml=" + sml +
            ", appStatus=" + appStatus +
            ", gauge=" + gauge +
            ", damage=" + damage +
            ", userId=" + userId +
            ", damageCode=" + damageCode +
            ", damageString=" + damageString +
            ", weather=" + weather.name() +
            ", terrain=" + terrain.name() +
            ", userStatus=" + userStatus +
            ", enemyStatus=" + enemyStatus +
            ", hp=" + Arrays.toString(hp) +
            ", mod=" + Arrays.toString(mod) +
            ", stats=" + Arrays.deepToString(stats) +
            ", buffs=" + Arrays.deepToString(buffs) +
            ", sstatus=" + Arrays.deepToString(interference) +
            ", skills=" + skills +
            '}';
    }

    public enum AppStatus {
        STARTED,
        TRAINER_CHOSEN,
        UNIT_CHOSEN,
        MOVE_CHOSEN,
        TARGETS_CHOSEN,
        ABLE_TO_CALCULATE;

        public AppStatus next() {
            return of(ordinal() + 1);
        }

        public static AppStatus of(int i) {
            for (AppStatus s : values()) {
                if (s.ordinal() == i) {
                    return s;
                }
            }
            return ABLE_TO_CALCULATE;
        }
    }

    public enum Weather {
        NORMAL("the weather was normal"),
        SUN("the weather was sunny"),
        RAIN("it was raining"),
        SANDSTORM("there was a sandstorm"),
        HAIL("it was hailing");

        final String s;

        Weather(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public enum Status {
        NORMAL,
        PARALYZE,
        POISON,
        BURN,
        SLEEP,
        FREEZE
    }

    public enum Terrain {
        NORMAL,
        ELECTRIC,
        GRASSY,
        MISTY,
        PSYCHIC;

        @Override
        public String toString() {
            return (this.equals(NORMAL) ? "no" : "the" + name().toLowerCase())+" terrain was active";
        }
    }
}
