package org.example.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author juhun_park
 * character와 deco의 다대다 매핑을 구현해주는 엔티티
 */
@Entity
public class Character_Deco {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long characterId;
    private Long decoId;

    public Long getDecoId() {
        return decoId;
    }

    public void setDecoId(Long decoId) {
        this.decoId = decoId;
    }

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }
}
