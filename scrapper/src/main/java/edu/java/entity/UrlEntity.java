package edu.java.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Entity(name = "url")
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class UrlEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String url;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastCheck;
    @ManyToMany(mappedBy = "urls", cascade = {
            CascadeType.MERGE
    })
    private Set<ChatEntity> chats;

    public UrlEntity(Long id, String url, OffsetDateTime lastUpdate, OffsetDateTime lastCheck) {
        this.id = id;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.lastCheck = lastCheck;
    }


}


