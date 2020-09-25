package com.cg.alterman.entity.item;

public class Signpost extends Item {
    public Signpost() {
        super("signpost.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "Do you wish to read the signpost?",
                "It says 'Use the Arrow Keys / WASD to move'",
                null);
    }
}
