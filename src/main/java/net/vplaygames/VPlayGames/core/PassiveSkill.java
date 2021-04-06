package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.caches.DataCache;
import com.vplaygames.PM4J.exceptions.ParseException;
import com.vplaygames.PM4J.jsonFramework.ParsableJSONObject;
import com.vplaygames.PM4J.util.MiscUtil;
import org.json.simple.JSONObject;

public class PassiveSkill implements ParsableJSONObject<PassiveSkill> {
    public final String name;
    public final String condition;
    public final int checks;
    public final int modifies;

    public PassiveSkill(String name, String condition, int checks, int modifies) {
        this.name = name;
        this.condition = condition;
        this.checks = checks;
        this.modifies = modifies;
        Data.instance.put(name, this);
    }

    @Override
    public String getAsJSON() {
        return "{\"condition\":\""+condition+"\", \"checks\":"+checks+", \"modifies\":"+modifies;
    }

    @Override
    public PassiveSkill parseFromJSON(String JSON) throws ParseException {
        return parse(JSON);
    }

    public static PassiveSkill parse(String JSON) throws ParseException {
        return parse(MiscUtil.parseJSONObject(JSON));
    }

    public static PassiveSkill parse(JSONObject jo) {
        String name = (String) jo.get("name");
        String condition = (String) jo.get("condition");
        String t_modifies = (String) jo.get("modifies");
        String t_checks = (String) jo.get("checks");
        int modifies = t_modifies.equals("m") ? 1 : t_modifies.equals("s") ? 2 : 3;
        int checks = t_checks.equals("u") ? 0 : t_checks.equals("t") ? 1 : 2;
        return new PassiveSkill(name, condition, checks, modifies);
    }

    public static class Data extends DataCache<Data, PassiveSkill> {
        public static Data instance = new Data();
        @Override
        protected void process0() {}
    }
}
