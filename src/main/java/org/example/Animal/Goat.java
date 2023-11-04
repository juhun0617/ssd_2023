package org.example.Animal;

import org.example.Entity.Character;

public class Goat extends Animal{
    public Goat(Character character) {
        super(character);
    }

    public final String PATH = "/Image/character/goat.png";

    @Override
    public String getPATH() {
        return PATH;
    }
}
