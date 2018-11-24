package com.lostshard.lostshard.plot;

import com.lostshard.lostshard.Objects.Groups.Clan;

public enum PlotFlag {
    BLACKSMITH("lostshard.plot.admin"),
    STAMINA("lostshard.plot.admin"),
    MANA("lostshard.plot.admin"),
    SHIRINE("lostshard.plot.admin"),
    PROTECTION("lostshard.plot"),
    EXPLOSIONS("lostshard.plot"),
    PRIVATE("lostshard.plot"),
    FRIENDBUILD("lostshard.plot"),
    TITLE("lostshard.plot.admin"),
    NOPVP("lostshard.plot.admin"),
    NOMAGIC("lostshard.plot.admin");

    private String permission;
    private PlotFlag[] dependencies;

    PlotFlag(String permission, PlotFlag[] dependencies) {
        this.permission = permission;
        this.dependencies = dependencies;
    }

    PlotFlag(String permission) {
        this(permission, null);
    }
}
