package vn.iotstar.model;

import java.io.Serializable;

public class Rating_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userid;
    private int bookid;
    private int rating;
    private String review_text;

    // Các trường hỗ trợ hiển thị
    private String user_fullname;
    private String user_email;

    public Rating_24133034() {
    }

    public Rating_24133034(int userid, int bookid, int rating, String review_text) {
        this.userid = userid;
        this.bookid = bookid;
        this.rating = rating;
        this.review_text = review_text;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
