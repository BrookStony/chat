/**
 * This class is generated by jOOQ
 */
package com.seecent.chat.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.2.3" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChatChatMessageRecord extends org.jooq.impl.UpdatableRecordImpl<com.seecent.chat.jooq.tables.records.ChatChatMessageRecord> {

	private static final long serialVersionUID = -1514495345;

	/**
	 * Setter for <code>chatdb.chat_chat_message.id</code>.
	 */
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.id</code>.
	 */
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.account_id</code>.
	 */
	public void setAccountId(java.lang.Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.account_id</code>.
	 */
	public java.lang.Long getAccountId() {
		return (java.lang.Long) getValue(1);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.date_created</code>.
	 */
	public void setDateCreated(java.sql.Timestamp value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.date_created</code>.
	 */
	public java.sql.Timestamp getDateCreated() {
		return (java.sql.Timestamp) getValue(2);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.errcode</code>.
	 */
	public void setErrcode(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.errcode</code>.
	 */
	public java.lang.Integer getErrcode() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.errmsg</code>.
	 */
	public void setErrmsg(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.errmsg</code>.
	 */
	public java.lang.String getErrmsg() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.from_user_id</code>.
	 */
	public void setFromUserId(java.lang.Long value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.from_user_id</code>.
	 */
	public java.lang.Long getFromUserId() {
		return (java.lang.Long) getValue(5);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.last_updated</code>.
	 */
	public void setLastUpdated(java.sql.Timestamp value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.last_updated</code>.
	 */
	public java.sql.Timestamp getLastUpdated() {
		return (java.sql.Timestamp) getValue(6);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.msg_time</code>.
	 */
	public void setMsgTime(java.lang.Long value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.msg_time</code>.
	 */
	public java.lang.Long getMsgTime() {
		return (java.lang.Long) getValue(7);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.msgid</code>.
	 */
	public void setMsgid(java.lang.Long value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.msgid</code>.
	 */
	public java.lang.Long getMsgid() {
		return (java.lang.Long) getValue(8);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.status</code>.
	 */
	public void setStatus(java.lang.Integer value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.status</code>.
	 */
	public java.lang.Integer getStatus() {
		return (java.lang.Integer) getValue(9);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.to_user_id</code>.
	 */
	public void setToUserId(java.lang.Long value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.to_user_id</code>.
	 */
	public java.lang.Long getToUserId() {
		return (java.lang.Long) getValue(10);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.type</code>.
	 */
	public void setType(java.lang.Integer value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.type</code>.
	 */
	public java.lang.Integer getType() {
		return (java.lang.Integer) getValue(11);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.class</code>.
	 */
	public void setClass_(java.lang.String value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.class</code>.
	 */
	public java.lang.String getClass_() {
		return (java.lang.String) getValue(12);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.description</code>.
	 */
	public void setDescription(java.lang.String value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.description</code>.
	 */
	public java.lang.String getDescription() {
		return (java.lang.String) getValue(13);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.title</code>.
	 */
	public void setTitle(java.lang.String value) {
		setValue(14, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.title</code>.
	 */
	public java.lang.String getTitle() {
		return (java.lang.String) getValue(14);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.url</code>.
	 */
	public void setUrl(java.lang.String value) {
		setValue(15, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.url</code>.
	 */
	public java.lang.String getUrl() {
		return (java.lang.String) getValue(15);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.content</code>.
	 */
	public void setContent(java.lang.String value) {
		setValue(16, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.content</code>.
	 */
	public java.lang.String getContent() {
		return (java.lang.String) getValue(16);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.hqmusicurl</code>.
	 */
	public void setHqmusicurl(java.lang.String value) {
		setValue(17, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.hqmusicurl</code>.
	 */
	public java.lang.String getHqmusicurl() {
		return (java.lang.String) getValue(17);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.musicurl</code>.
	 */
	public void setMusicurl(java.lang.String value) {
		setValue(18, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.musicurl</code>.
	 */
	public java.lang.String getMusicurl() {
		return (java.lang.String) getValue(18);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.thumb_media_id</code>.
	 */
	public void setThumbMediaId(java.lang.String value) {
		setValue(19, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.thumb_media_id</code>.
	 */
	public java.lang.String getThumbMediaId() {
		return (java.lang.String) getValue(19);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.media_id</code>.
	 */
	public void setMediaId(java.lang.String value) {
		setValue(20, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.media_id</code>.
	 */
	public java.lang.String getMediaId() {
		return (java.lang.String) getValue(20);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.voice_format</code>.
	 */
	public void setVoiceFormat(java.lang.String value) {
		setValue(21, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.voice_format</code>.
	 */
	public java.lang.String getVoiceFormat() {
		return (java.lang.String) getValue(21);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.picurl</code>.
	 */
	public void setPicurl(java.lang.String value) {
		setValue(22, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.picurl</code>.
	 */
	public java.lang.String getPicurl() {
		return (java.lang.String) getValue(22);
	}

	/**
	 * Setter for <code>chatdb.chat_chat_message.material_id</code>.
	 */
	public void setMaterialId(java.lang.Long value) {
		setValue(23, value);
	}

	/**
	 * Getter for <code>chatdb.chat_chat_message.material_id</code>.
	 */
	public java.lang.Long getMaterialId() {
		return (java.lang.Long) getValue(23);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Long> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ChatChatMessageRecord
	 */
	public ChatChatMessageRecord() {
		super(com.seecent.chat.jooq.tables.ChatChatMessage.CHAT_CHAT_MESSAGE);
	}

	/**
	 * Create a detached, initialised ChatChatMessageRecord
	 */
	public ChatChatMessageRecord(java.lang.Long id, java.lang.Long accountId, java.sql.Timestamp dateCreated, java.lang.Integer errcode, java.lang.String errmsg, java.lang.Long fromUserId, java.sql.Timestamp lastUpdated, java.lang.Long msgTime, java.lang.Long msgid, java.lang.Integer status, java.lang.Long toUserId, java.lang.Integer type, java.lang.String class_, java.lang.String description, java.lang.String title, java.lang.String url, java.lang.String content, java.lang.String hqmusicurl, java.lang.String musicurl, java.lang.String thumbMediaId, java.lang.String mediaId, java.lang.String voiceFormat, java.lang.String picurl, java.lang.Long materialId) {
		super(com.seecent.chat.jooq.tables.ChatChatMessage.CHAT_CHAT_MESSAGE);

		setValue(0, id);
		setValue(1, accountId);
		setValue(2, dateCreated);
		setValue(3, errcode);
		setValue(4, errmsg);
		setValue(5, fromUserId);
		setValue(6, lastUpdated);
		setValue(7, msgTime);
		setValue(8, msgid);
		setValue(9, status);
		setValue(10, toUserId);
		setValue(11, type);
		setValue(12, class_);
		setValue(13, description);
		setValue(14, title);
		setValue(15, url);
		setValue(16, content);
		setValue(17, hqmusicurl);
		setValue(18, musicurl);
		setValue(19, thumbMediaId);
		setValue(20, mediaId);
		setValue(21, voiceFormat);
		setValue(22, picurl);
		setValue(23, materialId);
	}
}
