package vn.iotstar.service;

import vn.iotstar.model.Author_24133034;
import java.util.List;

public interface IAuthorService_24133034 {
    List<Author_24133034> findAll();
    Author_24133034 findById(int id);
    List<Author_24133034> findByBookId(int bookId);
    boolean insert(Author_24133034 author);
    boolean setAuthorsForBook(int bookId, List<Integer> authorIds);
}
