package com.shanchain.shandata.db;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.shanchain.shandata.ui.model.MessageEntry;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_ENTRY".
*/
public class MessageEntryDao extends AbstractDao<MessageEntry, Long> {

    public static final String TABLENAME = "MESSAGE_ENTRY";

    /**
     * Properties of entity MessageEntry.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property RoomId = new Property(1, String.class, "roomId", false, "ROOM_ID");
        public final static Property MsgId = new Property(2, String.class, "msgId", false, "MSG_ID");
        public final static Property UserId = new Property(3, Long.class, "userId", false, "USER_ID");
        public final static Property UserName = new Property(4, String.class, "userName", false, "USER_NAME");
        public final static Property Avatar = new Property(5, String.class, "avatar", false, "AVATAR");
        public final static Property JgUserName = new Property(6, String.class, "jgUserName", false, "JG_USER_NAME");
        public final static Property DisplayName = new Property(7, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property MessageText = new Property(8, String.class, "messageText", false, "MESSAGE_TEXT");
        public final static Property TimeString = new Property(9, long.class, "timeString", false, "TIME_STRING");
        public final static Property MessageType = new Property(10, String.class, "messageType", false, "MESSAGE_TYPE");
        public final static Property FileFormat = new Property(11, String.class, "fileFormat", false, "FILE_FORMAT");
        public final static Property MediaFilePath = new Property(12, String.class, "mediaFilePath", false, "MEDIA_FILE_PATH");
        public final static Property Duration = new Property(13, long.class, "duration", false, "DURATION");
        public final static Property Progress = new Property(14, String.class, "progress", false, "PROGRESS");
    }

    private Query<MessageEntry> conversationEntry_MMessageEntryListQuery;

    public MessageEntryDao(DaoConfig config) {
        super(config);
    }
    
    public MessageEntryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_ENTRY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ROOM_ID\" TEXT NOT NULL ," + // 1: roomId
                "\"MSG_ID\" TEXT UNIQUE ," + // 2: msgId
                "\"USER_ID\" INTEGER," + // 3: userId
                "\"USER_NAME\" TEXT," + // 4: userName
                "\"AVATAR\" TEXT NOT NULL ," + // 5: avatar
                "\"JG_USER_NAME\" TEXT NOT NULL ," + // 6: jgUserName
                "\"DISPLAY_NAME\" TEXT," + // 7: displayName
                "\"MESSAGE_TEXT\" TEXT," + // 8: messageText
                "\"TIME_STRING\" INTEGER NOT NULL ," + // 9: timeString
                "\"MESSAGE_TYPE\" TEXT," + // 10: messageType
                "\"FILE_FORMAT\" TEXT," + // 11: fileFormat
                "\"MEDIA_FILE_PATH\" TEXT," + // 12: mediaFilePath
                "\"DURATION\" INTEGER NOT NULL ," + // 13: duration
                "\"PROGRESS\" TEXT);"); // 14: progress
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_ENTRY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MessageEntry entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getRoomId());
 
        String msgId = entity.getMsgId();
        if (msgId != null) {
            stmt.bindString(3, msgId);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(4, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(5, userName);
        }
        stmt.bindString(6, entity.getAvatar());
        stmt.bindString(7, entity.getJgUserName());
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(8, displayName);
        }
 
        String messageText = entity.getMessageText();
        if (messageText != null) {
            stmt.bindString(9, messageText);
        }
        stmt.bindLong(10, entity.getTimeString());
 
        String messageType = entity.getMessageType();
        if (messageType != null) {
            stmt.bindString(11, messageType);
        }
 
        String fileFormat = entity.getFileFormat();
        if (fileFormat != null) {
            stmt.bindString(12, fileFormat);
        }
 
        String mediaFilePath = entity.getMediaFilePath();
        if (mediaFilePath != null) {
            stmt.bindString(13, mediaFilePath);
        }
        stmt.bindLong(14, entity.getDuration());
 
        String progress = entity.getProgress();
        if (progress != null) {
            stmt.bindString(15, progress);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MessageEntry entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getRoomId());
 
        String msgId = entity.getMsgId();
        if (msgId != null) {
            stmt.bindString(3, msgId);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(4, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(5, userName);
        }
        stmt.bindString(6, entity.getAvatar());
        stmt.bindString(7, entity.getJgUserName());
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(8, displayName);
        }
 
        String messageText = entity.getMessageText();
        if (messageText != null) {
            stmt.bindString(9, messageText);
        }
        stmt.bindLong(10, entity.getTimeString());
 
        String messageType = entity.getMessageType();
        if (messageType != null) {
            stmt.bindString(11, messageType);
        }
 
        String fileFormat = entity.getFileFormat();
        if (fileFormat != null) {
            stmt.bindString(12, fileFormat);
        }
 
        String mediaFilePath = entity.getMediaFilePath();
        if (mediaFilePath != null) {
            stmt.bindString(13, mediaFilePath);
        }
        stmt.bindLong(14, entity.getDuration());
 
        String progress = entity.getProgress();
        if (progress != null) {
            stmt.bindString(15, progress);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MessageEntry readEntity(Cursor cursor, int offset) {
        MessageEntry entity = new MessageEntry( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // roomId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // msgId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // userId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userName
            cursor.getString(offset + 5), // avatar
            cursor.getString(offset + 6), // jgUserName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // displayName
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // messageText
            cursor.getLong(offset + 9), // timeString
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // messageType
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // fileFormat
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // mediaFilePath
            cursor.getLong(offset + 13), // duration
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14) // progress
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MessageEntry entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRoomId(cursor.getString(offset + 1));
        entity.setMsgId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setUserName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAvatar(cursor.getString(offset + 5));
        entity.setJgUserName(cursor.getString(offset + 6));
        entity.setDisplayName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMessageText(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTimeString(cursor.getLong(offset + 9));
        entity.setMessageType(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFileFormat(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setMediaFilePath(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setDuration(cursor.getLong(offset + 13));
        entity.setProgress(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MessageEntry entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MessageEntry entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MessageEntry entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "mMessageEntryList" to-many relationship of ConversationEntry. */
    public List<MessageEntry> _queryConversationEntry_MMessageEntryList(String roomId) {
        synchronized (this) {
            if (conversationEntry_MMessageEntryListQuery == null) {
                QueryBuilder<MessageEntry> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RoomId.eq(null));
                conversationEntry_MMessageEntryListQuery = queryBuilder.build();
            }
        }
        Query<MessageEntry> query = conversationEntry_MMessageEntryListQuery.forCurrentThread();
        query.setParameter(0, roomId);
        return query.list();
    }

}
