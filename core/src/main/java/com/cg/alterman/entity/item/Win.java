package com.cg.alterman.entity.item;

public class Win extends Item {
    public Win() {
        super("win.png");
        setItemInfo();
    }

    private void setItemInfo() {
        super.setItemInfo(
                "GAME OVER!\n" +
                "Thanks for playing!\n" +
                "Enjoyed the game?",
                "Awesome! :D",
                "Yeah, I didn't get enough time to make it better, sorry about that!");
    }
}
