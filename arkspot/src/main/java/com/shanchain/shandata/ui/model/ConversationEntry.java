package com.shanchain.shandata.ui.model;

/**
 * Created by ${chenyn} on 2017/7/18.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.shanchain.shandata.db.DaoSession;
import com.shanchain.shandata.db.MessageEntryDao;
import com.shanchain.shandata.db.ConversationEntryDao;

@Entity
public class ConversationEntry {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    public String targetName;

    public Integer order;

    @ToMany(joinProperties = {
            @JoinProperty(name = "targetName", referencedName = "roomId")
    })
    private List<MessageEntry> mMessageEntryList;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 469727349)
    private transient ConversationEntryDao myDao;

    @Generated(hash = 1057112971)
    public ConversationEntry(Long id, @NotNull String targetName, Integer order) {
        this.id = id;
        this.targetName = targetName;
        this.order = order;
    }

    @Generated(hash = 914319620)
    public ConversationEntry() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1841410666)
    public List<MessageEntry> getMMessageEntryList() {
        if (mMessageEntryList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageEntryDao targetDao = daoSession.getMessageEntryDao();
            List<MessageEntry> mMessageEntryListNew = targetDao
                    ._queryConversationEntry_MMessageEntryList(targetName);
            synchronized (this) {
                if (mMessageEntryList == null) {
                    mMessageEntryList = mMessageEntryListNew;
                }
            }
        }
        return mMessageEntryList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1969033880)
    public synchronized void resetMMessageEntryList() {
        mMessageEntryList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 953391257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversationEntryDao() : null;
    }
}
