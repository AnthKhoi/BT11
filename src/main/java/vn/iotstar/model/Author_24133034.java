package vn.iotstar.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Author_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int author_id;
    private String author_name;
    private Date date_of_birth;

    // Danh sách sách của tác giả (tiện cho việc hiển thị trang Home)
    private List<Book_24133034> books;

    public Author_24133034() {
    }

    public Author_24133034(int author_id, String author_name, Date date_of_birth) {
        this.author_id = author_id;
        this.author_name = author_name;
        this.date_of_birth = date_of_birth;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public List<Book_24133034> getBooks() {
        return books;
    }

    public void setBooks(List<Book_24133034> books) {
        this.books = books;
    }
}
