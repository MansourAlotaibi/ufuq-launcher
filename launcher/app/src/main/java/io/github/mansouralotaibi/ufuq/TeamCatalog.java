package io.github.mansouralotaibi.ufuq;

import android.content.Context;

/** Small offline catalog verified against TheSportsDB team lookup pages on 2026-10-27. */
final class TeamCatalog {
    static final class Team {
        final String id, english, arabic, group;
        Team(String id, String english, String arabic, String group) {
            this.id = id; this.english = english; this.arabic = arabic; this.group = group;
        }
        String name(Context context) {
            return "ar".equals(context.getResources().getConfiguration().locale.getLanguage())
                    ? arabic : english;
        }
    }

    static final String SAUDI = "saudi";
    static final String INTERNATIONAL = "international";
    static final Team NONE = new Team("none", "None", "بدون", "none");
    static final Team[] TEAMS = {
            new Team("136013", "Al Hilal", "الهلال", SAUDI),
            new Team("136022", "Al Nassr", "النصر", SAUDI),
            new Team("136018", "Al Ittihad", "الاتحاد", SAUDI),
            new Team("137721", "Al Ahli", "الأهلي", SAUDI),
            new Team("136020", "Al Shabab", "الشباب", SAUDI),
            new Team("136011", "Al Fateh", "الفتح", SAUDI),
            new Team("136012", "Al Taawoun", "التعاون", SAUDI),
            new Team("136014", "Al Fayha", "الفيحاء", SAUDI),
            new Team("133738", "Real Madrid", "ريال مدريد", INTERNATIONAL),
            new Team("133739", "Barcelona", "برشلونة", INTERNATIONAL),
            new Team("133613", "Manchester City", "مانشستر سيتي", INTERNATIONAL),
            new Team("133604", "Arsenal", "أرسنال", INTERNATIONAL),
            new Team("133602", "Liverpool", "ليفربول", INTERNATIONAL),
            new Team("133612", "Manchester United", "مانشستر يونايتد", INTERNATIONAL),
            new Team("133714", "Bayern Munich", "بايرن ميونخ", INTERNATIONAL),
            new Team("133689", "Paris Saint-Germain", "باريس سان جيرمان", INTERNATIONAL),
            new Team("133676", "Juventus", "يوفنتوس", INTERNATIONAL)
    };

    static Team find(String id) {
        if (id == null || "none".equals(id)) return NONE;
        for (Team team : TEAMS) if (team.id.equals(id)) return team;
        return NONE;
    }

    static Team[] group(String group) {
        java.util.ArrayList<Team> out = new java.util.ArrayList<>();
        for (Team team : TEAMS) if (group.equals(team.group)) out.add(team);
        return out.toArray(new Team[out.size()]);
    }

    static String localizedName(Context context, String providerName) {
        if (providerName == null) return "";
        if (!"ar".equals(context.getResources().getConfiguration().locale.getLanguage()))
            return providerName;
        // The provider is authoritative. If it supplies Arabic, retain it exactly; the small catalog below
        // is only a fallback because TheSportsDB fixture payloads normally contain one Latin team label.
        if (containsArabic(providerName)) return providerName;
        String normalized = providerName.replace('-', ' ').replaceAll("\\s+", " ").trim();
        for (Team team : TEAMS) {
            String catalog = team.english.replace('-', ' ').replaceAll("\\s+", " ").trim();
            if (catalog.equalsIgnoreCase(normalized)) return team.arabic;
        }
        if ("Al-Kholood".equalsIgnoreCase(providerName)) return "الخلود";
        if ("Abha".equalsIgnoreCase(providerName)) return "أبها";
        if ("Al-Shamal".equalsIgnoreCase(providerName)) return "الشمال";
        if ("Racing de Santander".equalsIgnoreCase(providerName)) return "راسينغ سانتاندير";
        if ("Deportivo Alavés".equalsIgnoreCase(providerName)) return "ديبورتيفو ألافيس";
        return providerName;
    }

    private static boolean containsArabic(String value) {
        for (int i = 0; i < value.length(); i++) {
            Character.UnicodeBlock block = Character.UnicodeBlock.of(value.charAt(i));
            if (block == Character.UnicodeBlock.ARABIC
                    || block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A
                    || block == Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B) return true;
        }
        return false;
    }

    private TeamCatalog() {}
}
