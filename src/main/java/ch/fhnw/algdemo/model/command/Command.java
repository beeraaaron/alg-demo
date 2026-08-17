package ch.fhnw.algdemo.model.command;

import lombok.Getter;
import lombok.Setter;

public abstract class Command {
    @Getter
    private final String command;
    @Getter
    @Setter
    private int id;

    public Command(String command) {
        this.command = command;
    }
}
