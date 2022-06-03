package com.example.englishlearning.Model.EssayWriting;

public class WaitingEssay {
    private String user;
    private long idSubject;
    private long id;
    private String content;
    private String subject;

    public WaitingEssay(String user, long idSubject, long id, String content, String subject) {
        this.user = user;
        this.idSubject = idSubject;
        this.id = id;
        this.content = content;
        this.subject = subject;
    }

    public static WaitingEssay parse(String key, String content, String subject){
        String[] info = key.split("-");
        String user = info[0];
        long idSubject = Long.parseLong(info[1]);
        long id = Long.parseLong(info[2]);
        return new WaitingEssay(user, idSubject, id, content, subject);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(long idSubject) {
        this.idSubject = idSubject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
