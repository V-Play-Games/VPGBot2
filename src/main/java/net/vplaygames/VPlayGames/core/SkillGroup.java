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

import net.vplaygames.VPlayGames.util.Array;

import java.util.ArrayList;
import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.core.Damage.Status;
import static net.vplaygames.VPlayGames.core.Damage.Weather;

public class SkillGroup
{
    final PassiveSkill skill;
    final ArrayList<Integer> intensity;
    Damage d;

    public SkillGroup(PassiveSkill skill, int intensity) {
        this.skill = skill;
        (this.intensity = new ArrayList<>()).add(intensity);
    }

    @Override
    public String toString() {
        return "{\"skill\":" + skill + ", \"intensity\":" + intensity + "}";
    }

    public boolean isActive(Damage damn) {
        d=damn;
        boolean act1=(skill.modifies==1 && !d.getAttack().isSync) || (skill.modifies==2&&d.getAttack().isSync) || skill.modifies==3;
        int t = skill.checks,b;
        String r = skill.condition;
        boolean act2=false;
        boolean j=false;
        switch (r.charAt(r.length()-1)) {
            case '-': j = true;
            case '+': r = r.substring(0, r.length() - 1);
        }
        switch (r)
        {
            case "always":
            case "ub":   act2=true; break;
            case "wthr": act2 =!d.weather.equals(Weather.NORMAL); break;
            case "sun":  act2 = d.weather.equals(Weather.SUN); break;
            case "rain": act2 = d.weather.equals(Weather.RAIN); break;
            case "sand": act2 = d.weather.equals(Weather.SANDSTORM); break;
            case "hail": act2 = d.weather.equals(Weather.HAIL); break;
            case "stts": act2 =!d.getStatus(t==1).equals(Status.NORMAL); break;
            case "par":  act2 = d.getStatus(t==1).equals(Status.PARALYZE); break;
            case "slp":  act2 = d.getStatus(t==1).equals(Status.SLEEP); break;
            case "psn":  act2 = d.getStatus(t==1).equals(Status.POISON); break;
            case "brn":  act2 = d.getStatus(t==1).equals(Status.BURN); break;
            case "frz":  act2 = d.getStatus(t==1).equals(Status.FREEZE); break;
          //case "sstts": act2=Array.sumAll(d.sstatus[t])!=0; break;
            case "fln": act2=d.interference[t][0]==1; break;
            case "cf":  act2=d.interference[t][1]==1; break;
            case "trp": act2=d.interference[t][2]==1; break;
            case "ch": act2=d.mod[0]==1; break;
            case "se": act2=d.mod[1]==1; break;
            case "mg": act2=d.gauge>=1; break;
            case "atk": b=d.buffs[t][0]; act2=j?b<0:b>0; break;
            case "spa": b=d.buffs[t][1]; act2=j?b<0:b>0; break;
            case "def": b=d.buffs[t][2]; act2=j?b<0:b>0; break;
            case "spd": b=d.buffs[t][3]; act2=j?b<0:b>0; break;
            case "spe": b=d.buffs[t][4]; act2=j?b<0:b>0; break;
            case "acc": b=d.buffs[t][5]; act2=j?b<0:b>0; break;
            case "eva": b=d.buffs[t][6]; act2=j?b<0:b>0; break;
            case "hpp": act2=d.hp[t]>0; break;
        }
        return act1&&act2;
    }

    public void add(int a) {
        if (skill.intensive)
            intensity.add(a);
    }

    public String getPassiveString() {
        return skill.name + (skill.intensive ? " " + intensity.stream().collect(() -> new StringJoiner("+"), (sj, i) -> sj.add(i.toString()), StringJoiner::merge) : "");
    }

    public String getMultiplierString() {
        StringJoiner tor = new StringJoiner("+","+","");
        for (int i : intensity) tor.add(String.valueOf(getMultiplier(i)));
        return tor.toString();
    }

    public double getMultiplier() {
        return getMultiplier(intensity.stream().mapToInt(i -> i).sum());
    }

    public double getMultiplier(int intensity) {
        double tor;
        switch (skill.condition) {
            case "mg": tor=d.gauge*intensity/100.0; break;
            case "hppu": tor=d.hp[0]*intensity/100.0; break;
            case "hppt": tor=d.hp[1]*intensity/100.0; break;
            default:
                if (skill.intensive)
                    tor=intensity/10.0;
                else
                    tor=1+Math.abs(getStatDependency()/2.0);
        }
        return tor;
    }

    public int getStatDependency() {
        int t = skill.checks;
        int bi = Array.returnID(new String[]{"atk", "spa", "def", "spd", "spe", "acc", "eva"}, skill.condition.substring(0, skill.condition.length() - 1));
        return skill.condition.endsWith("-") ? Math.min(d.buffs[t][bi], 0) : Math.max(d.buffs[t][bi], 0);
    }

    public String getName() {
        return skill.name;
    }
}
