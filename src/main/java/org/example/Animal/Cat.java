package org.example.Animal;

import org.example.Entity.Character;

public class Cat extends Animal{
    public Cat(Character character) {
        super(character);
    }
    public final String PATH = "/Image/character/cat.png";

    @Override
    public String getPATH() {
        return PATH;
    }


}
