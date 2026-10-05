package vn.iotstar.model;

import java.io.Serializable;

public class BookAuthor_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookid;
    private int author_id;

    public BookAuthor_24133034() {
    }

    public BookAuthor_24133034(int bookid, int author_id) {
        this.bookid = bookid;
        this.author_id = author_id;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }
}
