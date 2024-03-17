/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.tables;

import edu.java.domain.jooq.DefaultSchema;
import edu.java.domain.jooq.Keys;
import edu.java.domain.jooq.tables.records.UrlRecord;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

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
public class Url extends TableImpl<UrlRecord> {

    /**
     * The reference instance of <code>URL</code>
     */
    public static final Url URL = new Url();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>URL.ID</code>.
     */
    public final TableField<UrlRecord, Integer> ID =
        createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>URL.URL</code>.
     */
    public final TableField<UrlRecord, String> URL_ =
        createField(DSL.name("URL"), SQLDataType.VARCHAR(1000000000), this, "");
    /**
     * The column <code>URL.LAST_UPDATE</code>.
     */
    public final TableField<UrlRecord, OffsetDateTime> LAST_UPDATE =
        createField(DSL.name("LAST_UPDATE"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false), this, "");
    /**
     * The column <code>URL.LAST_CHECK</code>.
     */
    public final TableField<UrlRecord, OffsetDateTime> LAST_CHECK = createField(
        DSL.name("LAST_CHECK"),
        SQLDataType.TIMESTAMPWITHTIMEZONE(6).nullable(false)
                   .defaultValue(DSL.field("LOCALTIMESTAMP", SQLDataType.TIMESTAMPWITHTIMEZONE)),
        this,
        ""
    );

    private Url(Name alias, Table<UrlRecord> aliased) {
        this(alias, aliased, null);
    }

    private Url(Name alias, Table<UrlRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>URL</code> table reference
     */
    public Url(String alias) {
        this(DSL.name(alias), URL);
    }

    /**
     * Create an aliased <code>URL</code> table reference
     */
    public Url(Name alias) {
        this(alias, URL);
    }

    /**
     * Create a <code>URL</code> table reference
     */
    public Url() {
        this(DSL.name("URL"), null);
    }

    public <O extends Record> Url(Table<O> child, ForeignKey<O, UrlRecord> key) {
        super(child, key, URL);
    }

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<UrlRecord> getRecordType() {
        return UrlRecord.class;
    }

    @Override
    @NotNull
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<UrlRecord, Integer> getIdentity() {
        return (Identity<UrlRecord, Integer>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<UrlRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_1;
    }

    @Override
    @NotNull
    public List<UniqueKey<UrlRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_14);
    }

    @Override
    @NotNull
    public Url as(String alias) {
        return new Url(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Url as(Name alias) {
        return new Url(alias, this);
    }

    @Override
    @NotNull
    public Url as(Table<?> alias) {
        return new Url(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Url rename(String name) {
        return new Url(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Url rename(Name name) {
        return new Url(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Url rename(Table<?> name) {
        return new Url(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row4<Integer, String, OffsetDateTime, OffsetDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Integer, ? super String, ? super OffsetDateTime, ? super OffsetDateTime, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
        Class<U> toType,
        Function4<? super Integer, ? super String, ? super OffsetDateTime, ? super OffsetDateTime, ? extends U> from
    ) {
        return convertFrom(toType, Records.mapping(from));
    }
}