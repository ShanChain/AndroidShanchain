package com.shanchain.shandata.ui.model;

/**
 * Created by ${chenyn} on 2017/7/18.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "conversation")
public class ConversationEntry extends Model {

    @Column(name = "Targetname")
    public String targetname;

    @Column(name = "Orders")
    public Integer order;


    public ConversationEntry() {
        super();
    }

    public ConversationEntry(String targetname) {
        this.targetname = targetname;
    }

    public ConversationEntry(String targetname, int order) {
        this.targetname = targetname;
        this.order = order;
    }

    public static ConversationEntry getTopConversation(int order) {
        return new Select().from(ConversationEntry.class)
                .where("Orders = ?", order).executeSingle();
    }

    public static ConversationEntry getConversationByTargetName(String targetname) {
        return new Select().from(ConversationEntry.class).where("Targetname = ?", targetname).executeSingle();
    }

    public List<MessageEntry> getMessageEntry() {
        return getMany(MessageEntry.class, "Conversation");
    }
}
