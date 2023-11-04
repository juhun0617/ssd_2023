package org.example.Animal;

import org.example.Entity.Character;

public class Duck extends Animal{
    public Duck(Character character) {
        super(character);
    }

    public final String PATH = "/Image/character/duck.png";

    @Override
    public String getPATH() {
        return PATH;
    }
}
