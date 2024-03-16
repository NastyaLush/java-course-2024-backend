/*
 * This file is generated by jOOQ.
 */
package edu.java.scrapper.domain.jooq.tables.records;


import edu.java.scrapper.domain.jooq.tables.Url;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class UrlRecord extends UpdatableRecordImpl<UrlRecord> implements Record4<Integer, String, OffsetDateTime, OffsetDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>URL.ID</code>.
     */
    public void setId(@Nullable Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>URL.ID</code>.
     */
    @Nullable
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>URL.URL</code>.
     */
    public void setUrl(@Nullable String value) {
        set(1, value);
    }

    /**
     * Getter for <code>URL.URL</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>URL.LAST_UPDATE</code>.
     */
    public void setLastUpdate(@NotNull OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>URL.LAST_UPDATE</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastUpdate() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>URL.LAST_CHECK</code>.
     */
    public void setLastCheck(@Nullable OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>URL.LAST_CHECK</code>.
     */
    @Nullable
    public OffsetDateTime getLastCheck() {
        return (OffsetDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Integer, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row4<Integer, String, OffsetDateTime, OffsetDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return Url.URL.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Url.URL.URL_;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Url.URL.LAST_UPDATE;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Url.URL.LAST_CHECK;
    }

    @Override
    @Nullable
    public Integer component1() {
        return getId();
    }

    @Override
    @Nullable
    public String component2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime component3() {
        return getLastUpdate();
    }

    @Override
    @Nullable
    public OffsetDateTime component4() {
        return getLastCheck();
    }

    @Override
    @Nullable
    public Integer value1() {
        return getId();
    }

    @Override
    @Nullable
    public String value2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime value3() {
        return getLastUpdate();
    }

    @Override
    @Nullable
    public OffsetDateTime value4() {
        return getLastCheck();
    }

    @Override
    @NotNull
    public UrlRecord value1(@Nullable Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public UrlRecord value2(@Nullable String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public UrlRecord value3(@NotNull OffsetDateTime value) {
        setLastUpdate(value);
        return this;
    }

    @Override
    @NotNull
    public UrlRecord value4(@Nullable OffsetDateTime value) {
        setLastCheck(value);
        return this;
    }

    @Override
    @NotNull
    public UrlRecord values(@Nullable Integer value1, @Nullable String value2, @NotNull OffsetDateTime value3, @Nullable OffsetDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UrlRecord
     */
    public UrlRecord() {
        super(Url.URL);
    }

    /**
     * Create a detached, initialised UrlRecord
     */
    @ConstructorProperties({ "id", "url", "lastUpdate", "lastCheck" })
    public UrlRecord(@Nullable Integer id, @Nullable String url, @NotNull OffsetDateTime lastUpdate, @Nullable OffsetDateTime lastCheck) {
        super(Url.URL);

        setId(id);
        setUrl(url);
        setLastUpdate(lastUpdate);
        setLastCheck(lastCheck);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised UrlRecord
     */
    public UrlRecord(edu.java.scrapper.domain.jooq.tables.pojos.Url value) {
        super(Url.URL);

        if (value != null) {
            setId(value.getId());
            setUrl(value.getUrl());
            setLastUpdate(value.getLastUpdate());
            setLastCheck(value.getLastCheck());
            resetChangedOnNotNull();
        }
    }
}
