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
        // LEVEL 1 - Tutorial (Velmi snadný úvod)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W                       W",
                "W                       W",
                "W           G           W",
                "W S       L###L       E W",
                "W#######  L   L  #######W",
                "W         L   L         W",
                "W         L   L         W",
                "W      G  L   L  G      W",
                "W#########L###L#########W",
                "W         L   L         W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 2 - The Bridge (Rovný most, žádné pasti)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W                       W",
                "W      G    G    G      W",
                "W   S L###############L W",
                "W#####L               L#W",
                "W     L               L W",
                "W     L    G     G    L W",
                "W     L  L#########L  L W",
                "W     L  L         L  L W",
                "W  E  L  L    E    L  L W",
                "W#####L##L#########L##L#W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 3 - The Pit (Mělká jáma, snadný únik)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W                       W",
                "W G                   G W",
                "W#L#                 #L#W",
                "W L                   L W",
                "W L S               E L W",
                "W######L         L######W",
                "W      L    G    L      W",
                "W      L   L#L   L      W",
                "W      L   L L   L      W",
                "W      L   L L   L      W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 4 - Zig Zag (Širší patra, méně běhání)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W      L         L      W",
                "W   G  L    S    L  G   W",
                "W######L        #L######W",
                "W      L         L      W",
                "W      L         L      W",
                "W######L         L######W",
                "W      L         L      W",
                "W  G   L         L  G E W",
                "W######L         L######W",
                "W      L         L      W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 5 - Cages (Otevřené klece)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W   G       G       G   W",
                "W  #L#     #L#     #L#  W",
                "W   L       L       L   W",
                "W   L       L       L   W",
                "W S L       L     E L   W",
                "W###########L###########W",
                "W           L           W",
                "W     G     L     G     W",
                "W###########L###########W",
                "W           L           W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 6 - Rope Master (Méně lan, více podlahy)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W                       W",
                "W S  ________________   W",
                "W### L              L   W",
                "W    L      G       L   W",
                "W    L    L###L     L   W",
                "W    L    L   L     L   W",
                "W    L    L G L   E L   W",
                "W    L####L###L#####L   W",
                "W    L              L   W",
                "W    L______________L   W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 7 - Towers (Propojené věže)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W G       G       G     W",
                "W###     ###     ###    W",
                "W #       #       #     W",
                "W #   S   #       #   E W",
                "W ##################### W",
                "W #       #       #     W",
                "W #       #       #     W",
                "W #       #       #     W",
                "W#########L#######L#####W",
                "W         L       L     W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 8 - The Grid (Méně stěn, více volnosti)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W L L L L L L L L L L L W",
                "W L L L L S L L L L L L W",
                "W L L L L   L L L L L L W",
                "W#######################W",
                "W           L           W",
                "W  G     G  L  G     G  W",
                "W ###   ### L ###   ### W",
                "W           L           W",
                "W     E     L     E     W",
                "W###########L###########W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 9 - Hardcore (Zjednodušená verze)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "W           S           W",
                "W######L#########L######W",
                "W      L         L      W",
                "W  G   L    G    L   G  W",
                "W######L    L    L######W",
                "W      L    L    L      W",
                "W      L    L    L      W",
                "W  E   L    L    L   E  W",
                "W######L#########L######W",
                "W      L         L      W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));

        // LEVEL 10 - Final Boss (Velká odměna, málo rizika)
        LEVELS.add(List.of(
                "WWWWWWWWWWWWWWWWWWWWWWWWW",
                "WGGGGGGGGGGGGGGGGGGGGGGGW",
                "W###########L###########W",
                "W           L           W",
                "W           L           W",
                "W     S     L     E     W",
                "W###########L###########W",
                "W           L           W",
                "W           L           W",
                "W           L           W",
                "W#######################W",
                "WWWWWWWWWWWWWWWWWWWWWWWWW"
        ));
    }
}