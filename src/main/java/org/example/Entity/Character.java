package org.example.Entity;


import javax.persistence.*;

@Entity
public class Character {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    private String name;
    private String animal;
    private int hungry;
    private int thirst;
    private int fun;
    private int health;
    private int level;
    private int money;
    private int max_score_1;
    private int max_score_2;
    private int max_score_3;
    private int max_score_4;
    private Long backId;
    public Long getBackId(){
        return backId;
    }

    public void setBackId(Long backId) {
        this.backId = backId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public int getHungry() {
        return hungry;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int hygiene) {
        this.fun = hygiene;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMax_score_1() {
        return max_score_1;
    }

    public void setMax_score_1(int max_score_1) {
        this.max_score_1 = max_score_1;
    }

    public int getMax_score_2() {
        return max_score_2;
    }

    public void setMax_score_2(int max_score_2) {
        this.max_score_2 = max_score_2;
    }

    public int getMax_score_3() {
        return max_score_3;
    }

    public void setMax_score_3(int max_score_3) {
        this.max_score_3 = max_score_3;
    }

    public int getMax_score_4() {
        return max_score_4;
    }

    public void setMax_score_4(int max_score_4) {
        this.max_score_4 = max_score_4;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
