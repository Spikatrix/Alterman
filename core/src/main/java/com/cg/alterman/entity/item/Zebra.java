package com.cg.alterman.entity.item;

public class Zebra extends Item {
    public Zebra() {
        super("zebra.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "You found a zebra toy on the ground\n" +
                        "Do you wish to keep it?",
                "You put the zebra in your pocket :)\n" +
                "Its skin looks great and well composed",
                "You throw the zebra back to the ground :(");
    }
}
