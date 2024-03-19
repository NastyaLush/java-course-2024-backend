package edu.java.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tracking_urls", joinColumns = @JoinColumn(name = "url_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Set<UrlEntity> urls;

    public ChatEntity(Long id, Long tgChatId) {
        this.id = id;
        this.tgChatId = tgChatId;
    }

    public void addUrl(UrlEntity urlEntity) {
        //todo
        if (urlEntity.getChats() == null) {
            urlEntity.setChats(Set.of(this));
        }
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
