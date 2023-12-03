package org.example.Entity;


import javax.persistence.*;
import java.util.List;

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

    private Long tableId;
    private Long chairId;
    private int xp;

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Long getTableId(){
        return tableId;
    }
    public void setTableId(Long tableId){
        this.tableId = tableId;
    }
    public Long getChairId(){
        return chairId;
    }
    public void setChairId(Long chairId){
        this.chairId = chairId;
    }
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

        if (hungry > 100){
            this.hungry = 100;
        } else if (hungry < 0){
            this.hungry = 0;
        } else {
            this.hungry = hungry;
        }
    }

    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        if (thirst > 100){
            this.thirst = 100;
        } else if (thirst < 0){
            this.thirst = 0;
        } else {
            this.thirst = thirst;
        }
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int fun) {
        if (fun > 100){
            this.fun = 100;
        } else if (fun < 0){
            this.fun = 0;
        } else {
            this.fun = fun;
        }

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 100){
            this.health = 100;
        } else if (health < 0){
            this.health = 0;
        } else {
            this.health = health;
        }
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
