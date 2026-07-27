package org.ofus.core.util.command;

import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommand extends RootCommand implements PlayerExecutable {

    private String playerOnlyMessage = "&cOnly players can use this command!";

    protected PlayerCommand(@NotNull String name) {
        super(name);
    }

    public PlayerCommand playerOnlyMessage(String message) {
        this.playerOnlyMessage = message;
        return this;
    }

    @Override
    public String getPlayerOnlyMessage() {
        return playerOnlyMessage;
    }
}
