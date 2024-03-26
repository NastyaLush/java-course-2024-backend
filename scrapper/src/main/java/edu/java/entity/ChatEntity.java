package edu.java.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

@Entity(name = "chat")
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long tgChatId;
    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "tracking_urls", joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id"))
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<UrlEntity> urls;

    public ChatEntity(Long id, Long tgChatId) {
        this.id = id;
        this.tgChatId = tgChatId;
    }

    public void addUrl(UrlEntity urlEntity) {
        urlEntity.getChats()
                 .add(this);

        this.getUrls()
            .add(urlEntity);

    }

    public void removeUrl(UrlEntity urlEntity) {

        urlEntity.getChats()
                 .remove(this);
        this.getUrls()
            .remove(urlEntity);

    }
}
