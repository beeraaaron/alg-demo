package ch.fhnw.algdemo.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Command {
    private final String command;
    @Setter
    private int id;

    public Command(String command) {
        this.command = command;
    }

    public Command(String command, int id) {
        this.command = command;
        this.id = id;
    }
}
