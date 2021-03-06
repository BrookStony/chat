/**
 * This class is generated by jOOQ
 */
package com.seecent.chat.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.2.3" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChatChatMessage extends org.jooq.impl.TableImpl<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord> {

	private static final long serialVersionUID = 815900767;

	/**
	 * The singleton instance of <code>chatdb.chat_chat_message</code>
	 */
	public static final com.seecent.chat.jooq.tables.ChatChatMessage CHAT_CHAT_MESSAGE = new com.seecent.chat.jooq.tables.ChatChatMessage();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord> getRecordType() {
		return com.seecent.chat.jooq.tables.records.ChatChatMessageRecord.class;
	}

	/**
	 * The column <code>chatdb.chat_chat_message.id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.account_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> ACCOUNT_ID = createField("account_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.date_created</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.sql.Timestamp> DATE_CREATED = createField("date_created", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.errcode</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Integer> ERRCODE = createField("errcode", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.errmsg</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> ERRMSG = createField("errmsg", org.jooq.impl.SQLDataType.VARCHAR.length(2000), this);

	/**
	 * The column <code>chatdb.chat_chat_message.from_user_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> FROM_USER_ID = createField("from_user_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The column <code>chatdb.chat_chat_message.last_updated</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.sql.Timestamp> LAST_UPDATED = createField("last_updated", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.msg_time</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> MSG_TIME = createField("msg_time", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.msgid</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> MSGID = createField("msgid", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The column <code>chatdb.chat_chat_message.status</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Integer> STATUS = createField("status", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.to_user_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> TO_USER_ID = createField("to_user_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * The column <code>chatdb.chat_chat_message.type</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Integer> TYPE = createField("type", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.class</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> CLASS = createField("class", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this);

	/**
	 * The column <code>chatdb.chat_chat_message.description</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.title</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.url</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> URL = createField("url", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.content</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> CONTENT = createField("content", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.hqmusicurl</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> HQMUSICURL = createField("hqmusicurl", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.musicurl</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> MUSICURL = createField("musicurl", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.thumb_media_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> THUMB_MEDIA_ID = createField("thumb_media_id", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.media_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> MEDIA_ID = createField("media_id", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.voice_format</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> VOICE_FORMAT = createField("voice_format", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.picurl</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.String> PICURL = createField("picurl", org.jooq.impl.SQLDataType.VARCHAR.length(255), this);

	/**
	 * The column <code>chatdb.chat_chat_message.material_id</code>.
	 */
	public final org.jooq.TableField<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> MATERIAL_ID = createField("material_id", org.jooq.impl.SQLDataType.BIGINT, this);

	/**
	 * Create a <code>chatdb.chat_chat_message</code> table reference
	 */
	public ChatChatMessage() {
		super("chat_chat_message", com.seecent.chat.jooq.Chatdb.CHATDB);
	}

	/**
	 * Create an aliased <code>chatdb.chat_chat_message</code> table reference
	 */
	public ChatChatMessage(java.lang.String alias) {
		super(alias, com.seecent.chat.jooq.Chatdb.CHATDB, com.seecent.chat.jooq.tables.ChatChatMessage.CHAT_CHAT_MESSAGE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, java.lang.Long> getIdentity() {
		return com.seecent.chat.jooq.Keys.IDENTITY_CHAT_CHAT_MESSAGE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord> getPrimaryKey() {
		return com.seecent.chat.jooq.Keys.KEY_CHAT_CHAT_MESSAGE_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord>>asList(com.seecent.chat.jooq.Keys.KEY_CHAT_CHAT_MESSAGE_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord, ?>>asList(com.seecent.chat.jooq.Keys.FKB72167A771B2504F, com.seecent.chat.jooq.Keys.FKB72167A710AE620);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public com.seecent.chat.jooq.tables.ChatChatMessage as(java.lang.String alias) {
		return new com.seecent.chat.jooq.tables.ChatChatMessage(alias);
	}
}
