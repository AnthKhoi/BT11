package vn.iotstar.service.impl;

import vn.iotstar.dao.IAuthorDao_24133034;
import vn.iotstar.dao.impl.AuthorDaoImpl_24133034;
import vn.iotstar.model.Author_24133034;
import vn.iotstar.service.IAuthorService_24133034;

import java.util.List;

public class AuthorServiceImpl_24133034 implements IAuthorService_24133034 {

    private final IAuthorDao_24133034 authorDao = new AuthorDaoImpl_24133034();

    @Override
    public List<Author_24133034> findAll() {
        return authorDao.findAll();
    }

    @Override
    public Author_24133034 findById(int id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author_24133034> findByBookId(int bookId) {
        return authorDao.findByBookId(bookId);
    }

    @Override
    public boolean insert(Author_24133034 author) {
        return authorDao.insert(author);
    }

    @Override
    public boolean setAuthorsForBook(int bookId, List<Integer> authorIds) {
        authorDao.removeAuthorsByBookId(bookId);
        if (authorIds != null) {
            for (Integer aid : authorIds) {
                authorDao.addAuthorToBook(bookId, aid);
            }
        }
        return true;
    }
}
