package util;

import java.util.Date;

public class Message {
    private String messageUser;
    private String messageText;
    private String messageUserId;
    private String gId;
    private long messageTime;

    public Message(String messageUser, String messageText, String messageUserId, String gId) {
        this.messageUser = messageUser;
        this.messageText = messageText;
        this.messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
        this.gId = gId;
    }

    public Message(){}

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getgId() {
        return gId;
    }
}
