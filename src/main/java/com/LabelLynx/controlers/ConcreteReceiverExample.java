package com.LabelLynx.controlers;

public class ConcreteReceiverExample extends Command {

    @Override
    public boolean execute() {
        System.out.println("Yesss");
        return true;
    }

    @Override
    public void undo() {

    }
}
