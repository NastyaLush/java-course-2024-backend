/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.17.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TrackingUrls implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer urlId;
    private Integer chatId;

    public TrackingUrls() {
    }

    public TrackingUrls(TrackingUrls value) {
        this.id = value.id;
        this.urlId = value.urlId;
        this.chatId = value.chatId;
    }

    @ConstructorProperties({"id", "urlId", "chatId"})
    public TrackingUrls(
        @NotNull Integer id,
        @Nullable Integer urlId,
        @Nullable Integer chatId
    ) {
        this.id = id;
        this.urlId = urlId;
        this.chatId = chatId;
    }

    /**
     * Getter for <code>TRACKING_URLS.ID</code>.
     */
    @NotNull
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>TRACKING_URLS.ID</code>.
     */
    public void setId(@NotNull Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>TRACKING_URLS.URL_ID</code>.
     */
    @Nullable
    public Integer getUrlId() {
        return this.urlId;
    }

    /**
     * Setter for <code>TRACKING_URLS.URL_ID</code>.
     */
    public void setUrlId(@Nullable Integer urlId) {
        this.urlId = urlId;
    }

    /**
     * Getter for <code>TRACKING_URLS.CHAT_ID</code>.
     */
    @Nullable
    public Integer getChatId() {
        return this.chatId;
    }

    /**
     * Setter for <code>TRACKING_URLS.CHAT_ID</code>.
     */
    public void setChatId(@Nullable Integer chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrackingUrls other = (TrackingUrls) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.urlId == null) {
            if (other.urlId != null) {
                return false;
            }
        } else if (!this.urlId.equals(other.urlId)) {
            return false;
        }
        if (this.chatId == null) {
            if (other.chatId != null) {
                return false;
            }
        } else if (!this.chatId.equals(other.chatId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.urlId == null) ? 0 : this.urlId.hashCode());
        result = prime * result + ((this.chatId == null) ? 0 : this.chatId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TrackingUrls (");

        sb.append(id);
        sb.append(", ").append(urlId);
        sb.append(", ").append(chatId);

        sb.append(")");
        return sb.toString();
    }
}