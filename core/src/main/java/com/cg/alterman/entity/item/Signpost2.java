package com.cg.alterman.entity.item;

public class Signpost2 extends Item {
    public Signpost2() {
        super("signpost.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "Do you wish to read the signpost?",
                "It says 'Where there is a will, there is a way!'",
                null);
    }
}
