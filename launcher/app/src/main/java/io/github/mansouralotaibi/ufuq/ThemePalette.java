package io.github.mansouralotaibi.ufuq;

/** Immutable, allocation-free palette selection. Unknown saved IDs fall back to Classic. */
final class ThemePalette {
    static final String[] IDS = {"classic", "midnight", "coast"};
    static final int[] ACCENTS = {0xFFF2B25B, 0xFFC7D5E5, 0xFF4DD9D0};
    static final int[] SURFACES = {0xFF18130F, 0xFF10151F, 0xFF071B29};
    // Classic matches the approved 2.8.5 ReflectionView exactly; variants change tint only.
    static final int[] REFLECTION_TOP = {0x48142C3A, 0x48192336, 0x48102F3C};
    static final int[] REFLECTION_BOTTOM = {0x18131D2A, 0x18101722, 0x180B2130};
    static final int REFLECTION_BORDER = 0x1FFBF4E8;
    static int index(String id) {
        for (int i = 0; i < IDS.length; i++) if (IDS[i].equals(id)) return i;
        return 0;
    }
    private ThemePalette() {}
}
