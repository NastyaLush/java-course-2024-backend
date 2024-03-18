package edu.java.repository.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.jooq.tables.records.ChatRecord;
import edu.java.entity.ChatEntity;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.TgChatRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqTgChatRepository implements TgChatRepository {
    private final DSLContext dsl;

    @Autowired
    public JooqTgChatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override public long add(long tgChatId) {
        ChatRecord chatRecord =
            dsl.insertInto(Tables.CHAT).columns(Tables.CHAT.TG_CHAT_ID).values((int) tgChatId).onConflictDoNothing()
               .returning(Tables.CHAT.ID).fetchOne();
        if (chatRecord == null) {
            throw new AlreadyExistException("Chat already exists");
        }
        return chatRecord.getId().longValue();
    }

    @Override public long remove(long tgChatId) {
        ChatRecord chatRecord =
            dsl.deleteFrom(Tables.CHAT).where(Tables.CHAT.TG_CHAT_ID.equal((int) tgChatId)).returning(Tables.CHAT.ID)
               .fetchOne();
        if (chatRecord == null) {
            throw new NotExistException("this chat is not exist");
        }
        return chatRecord.getId().longValue();
    }

    @Override public List<ChatEntity> findAll() {
        return dsl.select(Tables.CHAT.fields()).from(Tables.CHAT).fetchInto(ChatEntity.class);
    }

    @Override public Optional<ChatEntity> findByTgId(long chatTgId) {
        return dsl.selectFrom(Tables.CHAT).where(Tables.CHAT.TG_CHAT_ID.equal((int) chatTgId)).fetchOptional()
                  .map(chatRecord -> new ChatEntity(
                      Long.valueOf(chatRecord.get(Tables.CHAT.ID)),
                      Long.valueOf(chatRecord.get(Tables.CHAT.TG_CHAT_ID))
                  ));
    }
}
