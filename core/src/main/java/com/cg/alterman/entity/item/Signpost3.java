package com.cg.alterman.entity.item;

public class Signpost3 extends Item {
    public Signpost3() {
        super("signpost.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "Do you wish to read the signpost?",
                "It says 'Congratulations for exploiting a bug in the game\n" +
                "which I don't have time to fix :p'",
                null);
    }
}
