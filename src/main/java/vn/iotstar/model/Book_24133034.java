package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Book_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookid;
    private int isbn;
    private String title;
    private String publisher;
    private BigDecimal price;
    private String description;
    private Date publish_date;
    private String cover_image;
    private int quantity;

    // Các trường hỗ trợ hiển thị giao diện (View Model)
    private String author_name; // Tên tác giả đại diện
    private List<Author_24133034> authors; // Danh sách các tác giả
    private int review_count; // Số lượt đánh giá
    private double rating_avg; // Điểm đánh giá trung bình

    public Book_24133034() {
    }

    public Book_24133034(int bookid, int isbn, String title, String publisher, BigDecimal price, String description, Date publish_date, String cover_image, int quantity) {
        this.bookid = bookid;
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.price = price;
        this.description = description;
        this.publish_date = publish_date;
        this.cover_image = cover_image;
        this.quantity = quantity;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public List<Author_24133034> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author_24133034> authors) {
        this.authors = authors;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public double getRating_avg() {
        return rating_avg;
    }

    public void setRating_avg(double rating_avg) {
        this.rating_avg = rating_avg;
    }
}
