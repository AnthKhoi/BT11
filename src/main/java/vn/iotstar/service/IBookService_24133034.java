package vn.iotstar.service;

import vn.iotstar.model.Book_24133034;
import java.util.List;

public interface IBookService_24133034 {
    List<Book_24133034> findAll(int page, int pageSize);
    int countAll();
    Book_24133034 findById(int bookid);
    boolean insert(Book_24133034 book, List<Integer> authorIds);
    boolean update(Book_24133034 book, List<Integer> authorIds);
    boolean delete(int bookid);

    // Phân trang 03 sản phẩm / trang theo từng tác giả (Câu 3)
    List<Book_24133034> findByAuthorId(int authorId, int page, int pageSize);
    int countByAuthorId(int authorId);
}
