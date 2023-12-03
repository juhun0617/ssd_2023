package org.example.game.Mulgoging;

import javax.swing.*;
import java.awt.*;

public class Character_Mulgoging {
    private String name;
    private Image image;

    public Character_Mulgoging(String name) {
        this.name = name;
        this.image = loadImage(name);
    }

    private Image loadImage(String characterName) {
        String imagePath;
        switch(characterName) {
            case "rabbit":
                imagePath = "src/Image/animalimg/Rabbit_1.png";
                break;
            case "goat":
                imagePath = "src/Image/animalimg/염소.png";
                break;
            case "duck":
                imagePath = "src/Image/animalimg/오리.png";
                break;
            case "cat":
                imagePath = "src/Image/animalimg/고양이.png";
                break;
            default:
                imagePath = "path/to/default/image.png";
                break;
        }
        return new ImageIcon(imagePath).getImage();
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
