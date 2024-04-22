package com.LabelLynx.controlers;

public abstract class Command {
    public abstract boolean execute();

    public abstract void undo();
}
