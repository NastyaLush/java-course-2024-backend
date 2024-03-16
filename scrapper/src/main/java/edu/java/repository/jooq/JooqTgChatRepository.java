package edu.java.repository.jooq;

import edu.java.repository.entity.ChatEntity;
import edu.java.repository.interf.TgChatRepository;
import edu.java.scrapper.domain.jooq.Tables;
import edu.java.scrapper.domain.jooq.tables.records.ChatRecord;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqTgChatRepository implements TgChatRepository {
    private DSLContext dsl;

    @Autowired
    public JooqTgChatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override public long add(long tgChatId) {
        ChatRecord chatRecord =
            dsl.insertInto(Tables.CHAT).columns(Tables.CHAT.TG_CHAT_ID).values((int) tgChatId).onConflictDoNothing()
               .returning(Tables.CHAT.ID).fetchOne();
        if (chatRecord == null) {
            throw new IllegalArgumentException("Chat already exists");
        }
        return chatRecord.getId().longValue();
    }

    @Override public long remove(long tgChatId) {
        return 0;
    }

    @Override public List<ChatEntity> findAll() {
        return dsl.select(Tables.CHAT.fields()).from(Tables.CHAT).fetchInto(ChatEntity.class);
    }

    @Override public Optional<ChatEntity> findByTgId(long chatTgId) {
        return Optional.empty();
    }
}
