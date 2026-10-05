package vn.iotstar.dao;

import vn.iotstar.model.Book_24133034;
import java.util.List;

public interface IBookDao_24133034 {
    List<Book_24133034> findAll(int page, int pageSize);
    int countAll();
    Book_24133034 findById(int bookid);
    int insert(Book_24133034 book);
    boolean update(Book_24133034 book);
    boolean delete(int bookid);
    
    // Yêu cầu Câu 3: Phân trang theo tác giả (3 sp/trang)
    List<Book_24133034> findByAuthorId(int authorId, int page, int pageSize);
    int countByAuthorId(int authorId);
}
