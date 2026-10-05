package vn.iotstar.dao;

import vn.iotstar.model.Author_24133034;
import java.util.List;

public interface IAuthorDao_24133034 {
    List<Author_24133034> findAll();
    Author_24133034 findById(int id);
    List<Author_24133034> findByBookId(int bookId);
    boolean insert(Author_24133034 author);
    boolean addAuthorToBook(int bookId, int authorId);
    boolean removeAuthorsByBookId(int bookId);
}
