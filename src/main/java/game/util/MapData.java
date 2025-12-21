package game.util;

import java.util.ArrayList;
import java.util.List;

public class MapData {

    public static List<String> getLevel(int index) {
        if (index < 0 || index >= LEVELS.size()) return LEVELS.get(0);
        return LEVELS.get(index);
    }

    public static int getLevelCount() {
        return LEVELS.size();
    }

    private static final List<List<String>> LEVELS = new ArrayList<>();

    static {
        // LEVEL 1 - Tutorial
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W                       W",
                "W            G          W",
                "W    S      ###         W",
                "W#######    L_L    E    W",
                "W      L    L L   ###   W",
                "W      L    L L   L L   W",
                "W  G   L  E L L G L L   W",
                "W######L#####L#L###L#   W",
                "W      L     L L   L    W",
                "W      L     L L   L    W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 2 - The Bridge
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W           G           W",
                "W   G  ___________  G   W",
                "W######L    L    L######W",
                "W      L    L    L      W",
                "W   S  L    L    L  E   W",
                "W######L    L    L######W",
                "W      L    L    L      W",
                "W      L  G L G  L      W",
                "W      L#########L      W",
                "W      L         L      W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 3 - The Pit
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W G                   G W",
                "W###                 ###W",
                "W L                   L W",
                "W L    G   ___   G    L W",
                "W L   ### L   L ###   L W",
                "W L   L L L   L L L   L W",
                "W L S L L L E L L L   L W",
                "W#####L L L###L L L#####W",
                "W     L L L   L L L     W",
                "W     L L L G L L L     W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 4 - Zig Zag
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W      L         L      W",
                "W   G  L    G    L  G   W",
                "W######L  #######L######W",
                "W      L  L      L      W",
                "W      L  L   S  L      W",
                "W######L  L######L      W",
                "W      L  L      L      W",
                "W   E  L  L   G  L  E   W",
                "W#######  #######L######W",
                "W                L      W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 5 - Cages
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W G   W   G   W   G   W W",
                "W###  W  ###  W  ###  W W",
                "W L   W   L   W   L   W W",
                "W L   W   L   W   L   W W",
                "W L   W   L   W   L   W W",
                "W L S     L E     L     W",
                "W#######################W",
                "W           L           W",
                "W     G     L     G     W",
                "W###########L###########W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 6 - Rope Master
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W S _________________   W",
                "W###L               L   W",
                "W   L  G         G  L   W",
                "W   L_###_______###_L   W",
                "W   L L           L L   W",
                "W   L L     E     L L   W",
                "W   L L###########L L   W",
                "W   L               L   W",
                "W   L_______G_______L   W",
                "W           #           W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 7 - Towers
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W G   L   G   L   G   L W",
                "W###  L  ###  L  ###  L W",
                "W  #  L    #  L    #  L W",
                "W  #  L    #  L    #  L W",
                "W  #  L    #  L    #  L W",
                "W  #  L S  #  L E  #  L W",
                "W#######################W",
                "W                       W",
                "W           G           W",
                "W#######################W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 8 - The Grid
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W#L#L#L#L#L#L#L#L#L#L#L#W",
                "W L L L L L L L L L L L W",
                "W L L L L S L L L L L L W",
                "W L G L L G L L G L L L W",
                "W#######################W",
                "W           L           W",
                "W           L           W",
                "W     E     L     E     W",
                "W___________L___________W",
                "W           L           W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 9 - Hardcore
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W            S          W",
                "W___________   _________W",
                "W   L      L   L        W",
                "W G L   G  L   L  G     W",
                "W###L######L###L########W",
                "W   L      L   L        W",
                "W E L      L E L        W",
                "W###L      L###L        W",
                "W   L   G  L   L  G     W",
                "W##########L############W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 10 - Final Boss
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "WGGGGGGGGGGGGGGGGGGGGGGGW",
                "W#######################W",
                "W     L           L     W",
                "W     L     S     L     W",
                "W     L___________L     W",
                "W     L           L     W",
                "W  E  L     E     L  E  W",
                "W#####L###########L#####W",
                "W     L           L     W",
                "W     L___________L     W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));
    }
}