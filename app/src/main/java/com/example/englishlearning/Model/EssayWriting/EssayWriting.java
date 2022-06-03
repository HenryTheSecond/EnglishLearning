package com.example.englishlearning.Model.EssayWriting;

public class EssayWriting {
    private String user;
    private long idSubject;
    private long id;
    private String content;
    private float point;
    private String feedback;
    private boolean isPointed = false;

    public EssayWriting(String user, long idSubject, long id, String content, float point, String feedback) {
        this.user = user;
        this.idSubject = idSubject;
        this.id = id;
        this.content = content;
        this.point = point;
        this.feedback = feedback;
    }

    public EssayWriting(String user, long idSubject, long id, String content) {
        this.user = user;
        this.idSubject = idSubject;
        this.id = id;
        this.content = content;
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

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isPointed() {
        return isPointed;
    }

    public void setPointed(boolean pointed) {
        isPointed = pointed;
    }
}
