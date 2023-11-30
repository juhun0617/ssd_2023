package org.example.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Character_Deco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long characterId;
    private Long decoId;

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public void setDecoId(Long decoId) {
        this.decoId = decoId;
    }

    public Long getDecoId() {
        return decoId;
    }

    public Long getCharacterId() {
        return characterId;
    }
}
