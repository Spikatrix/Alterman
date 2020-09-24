package com.cg.alterman.entity.item;

public class Jam extends Item {
    public Jam() {
        super("jam.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "You find a bottle of GDX flavored jam\n" +
                        "It says manufactured on Sept 20, 2020, Expires on Sept 26, 2020\n" +
                        "Do you want to eat it?",
                "You open the jar of jam and eat it!\n" +
                        "It tastes ... pretty good actually. Yummy!",
                "Yeah, who eats expired food anyway?");
    }
}
