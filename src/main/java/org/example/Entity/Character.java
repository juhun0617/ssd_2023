package org.example.Entity;


import javax.persistence.*;

@Entity
public class Character {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String animal;
    private int hungry;
    private int thirst;
    private int hygiene;
    private int health;
    private int level;
    private int money;
    private int max_score_1;
    private int max_score_2;
    private int max_score_3;
    private int max_score_4;



    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
