package com.cg.alterman.entity.item;

public class Paper2 extends Item {
    public Paper2() {
        super("paper.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo("You find a crumbled piece of paper on the ground\n" +
                        "Do you want to read it?",
                "It says 'One more leap and you're done!'",
                null);
    }
}
