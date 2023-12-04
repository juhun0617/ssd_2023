package org.example.Animal;

import org.example.Entity.Character;

public class Rabbit extends Animal{
    public Rabbit(Character character) {
        super(character);
    }

    public final String PATH = "/Image/character/rabbit.png";

    @Override
    public String getPATH() {
        return PATH;
    }


}
