package com.cg.alterman.entity.item;

public class Paper extends Item {
    public Paper() {
        super("paper.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo("You find a crumbled piece of paper on the ground\n" +
                        "Do you want to read it?",
                "It says 'Welcome to the dark side!'",
                null);
    }
}
