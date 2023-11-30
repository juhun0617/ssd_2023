package org.example.Entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Deco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String decoName;
    private String decoClass;
    private int decoPrice;
    private String decoImagePath;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public String getDecoName(){
        return decoName;
    }
    public String getDecoImagePath(){
        return decoImagePath;
    }
    public int getDecoPrice(){
        return decoPrice;
    }
    public String getDecoClass(){
        return decoClass;
    }
}
