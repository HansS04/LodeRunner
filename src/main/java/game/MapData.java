package game;

import java.util.ArrayList;
import java.util.List;

public class MapData {

    private static final String E = "W                       W";
    private static final String F = "WWWWWWWWWWWWWWWWWWWWWWWWW";

    public static final List<List<String>> ALL_LEVELS = new ArrayList<>();

    static {
        // --- LEVEL 1 (Tutorial) ---
        ALL_LEVELS.add(List.of(
                F, E, E, E, E,
                "W      G                W",
                "W______L_______     S   W",
                "W      L       LCCCCCCCCW",
                "W      L       L        W",
                "W   G  L   E   L   G    W",
                "WCCCCCCLCCCCCCCLCCCCCCCCW",
                "W      L       L        W",
                "W      L       L        W",
                "W      L       L        W",
                "W      L       L        W",
                "W E    L   G   L      E W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 2 (Více žebříků) ---
        ALL_LEVELS.add(List.of(
                F, E,
                "W  G    S      G      G W",
                "WCCCCCC   CCCCCC   CCCCCW",
                "W     L   L    L   L    W",
                "W     L   L    L   L    W",
                "W  E  L   L E  L   L  E W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                "W           L           W",
                "W   G       L       G   W",
                "WCCCCCCC    L    CCCCCCCW",
                "W      L    L    L      W",
                "W      L    L    L      W",
                "W  E   L    L    L   E  W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 3 (Lana a pasti) ---
        ALL_LEVELS.add(List.of(
                F, E,
                "W S   G        G      G W",
                "W______   __________   _W",
                "W      L  L        L  L W",
                "W      L  L        L  L W",
                "W   E  L  L   E    L  L W",
                "WCCCCCCC  CCCCCCCCCC  CCW",
                "W           L           W",
                "W           L           W",
                "W G         L         G W",
                "WCCCCCCCC   L    CCCCCCCW",
                "W           L           W",
                "W           L           W",
                "W    E      L      E    W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 4 (Pyramida) ---
        ALL_LEVELS.add(List.of(
                F, E,
                "W           G           W",
                "W           L           W",
                "W          CLC          W",
                "W         CCCLCC        W",
                "W        CCCCLCCCC      W",
                "W       CCCCCLCCCCC     W",
                "W      CCCCCCLCCCCCC    W",
                "W     CCCCCCCLCCCCCCC   W",
                "W    CCCCCCCCLCCCCCCCC  W",
                "W   CCCCCCCCCLCCCCCCCCC W",
                "W  S         L         EW",
                "W____________L__________W",
                "W G          L        GW",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 5 (Rozdělený svět) ---
        ALL_LEVELS.add(List.of(
                F,
                "W      |      L      |  W",
                "W  S   |  G   L   G  | E W",
                "WCC CC | CCCCCLCCCCC | CCW",
                "W   L  |      L      | L W",
                "W   L  |      L      | L W",
                "W___L__|______L______|___W",
                "W   L         L          W",
                "W   L         L          W",
                "W   L      E  L   E      W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                "W     G     L     G     W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                F
        ));

        // --- LEVEL 6 (Bludiště) ---
        ALL_LEVELS.add(List.of(
                F,
                "W S  L     L     L    GW",
                "WCC  L  C  L  C  L  CC W",
                "W    L  L  L  L  L     W",
                "W G  L  L  L  L  L  E  W",
                "WCCCCCC L  L  L  CCCCCCW",
                "W       L  L  L        W",
                "W_______L__L__L________W",
                "W       L     L        W",
                "W G   E L  G  L E    G W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                "W           L           W",
                "W           L           W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 7 (Dlouhý pád) ---
        ALL_LEVELS.add(List.of(
                F,
                "W S G                   W",
                "WCCCCCC                 W",
                "W     L                 W",
                "W     L       G         W",
                "W     L      CCC        W",
                "W     L       L         W",
                "W     L       L     G   W",
                "W     L       L    CCC  W",
                "W     L       L     L   W",
                "W     L       L     L   W",
                "W     L       L     L   W",
                "W  E  L   E   L  E  L   W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                F
        ));

        // --- LEVEL 8 (Dvojitá podlaha) ---
        ALL_LEVELS.add(List.of(
                F,
                "W G   S      L      G   W",
                "WCCCCCCCCCC  L  CCCCCCCCW",
                "W            L          W",
                "W            L          W",
                "W  G      E  L  E    G  W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W            L          W",
                "W            L          W",
                "W  G      E  L  E    G  W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W            L          W",
                "W            L          W",
                "W____________L__________W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));

        // --- LEVEL 9 (Mříž) ---
        ALL_LEVELS.add(List.of(
                F,
                "W S L  G  L  G  L  G  L W",
                "WCC L CCC L CCC L CCC L W",
                "W   L     L     L     L W",
                "W___L_____L_____L_____L_W",
                "W   L     L     L     L W",
                "WCC L CCC L CCC L CCC L W",
                "W   L     L     L     L W",
                "W   L  E  L  E  L  E  L W",
                "WCC L CCC L CCC L CCC L W",
                "W   L     L     L     L W",
                "W   L     L     L     L W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                "W           L           W",
                F
        ));

        // --- LEVEL 10 (Final Challenge) ---
        ALL_LEVELS.add(List.of(
                F,
                "W G  G  G    S    G  G GW",
                "WCCCCCCCC    L    CCCCCCW",
                "W       L    L    L     W",
                "W       L    L    L     W",
                "W   E   L    L    L  E  W",
                "WCCCCCCCLCCCCLCCCCLCCCCCW",
                "W       L    L    L     W",
                "W       L    L    L     W",
                "W   E   L    L    L  E  W",
                "WCCCCCCCLCCCCLCCCCLCCCCCW",
                "W       L    L    L     W",
                "W       L    L    L     W",
                "W G   E L E  L  E L E G W",
                "WCCCCCCCCCCCCCCCCCCCCCCCW",
                F
        ));
    }
}