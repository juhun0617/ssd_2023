package org.example.Entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author juhun_park
 * 꾸밈요소 엔티티 클래스
 */
@Entity
public class Deco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String decoName;
    private String decoClass;
    private int decoPrice;
    private String decoImagePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecoName() {
        return decoName;
    }

    public String getDecoImagePath() {
        return decoImagePath;
    }

    public int getDecoPrice() {
        return decoPrice;
    }

    public String getDecoClass() {
        return decoClass;
    }
}
