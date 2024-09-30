package xyz.someboringnerd.waifuhax.systems.modules;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public enum CATEGORY {
    COMBAT("Combat"),
    PLAYER("Player"),
    MOVEMENT("Movement"),
    EXPLOITS("Exploits"),
    MISC("Misc"),
    HUD("Hud"),
    RENDER("Render"),
    WORLD("World");

    @Getter(AccessLevel.PUBLIC)
    private String displayName;

    CATEGORY(String displayName) {
        this.displayName = displayName;
    }

    @Setter(AccessLevel.PUBLIC)
    @Getter(AccessLevel.PUBLIC)
    private boolean active = true;


}
