package vn.iotstar.service.impl;

import vn.iotstar.dao.IAuthorDao_24133034;
import vn.iotstar.dao.IBookDao_24133034;
import vn.iotstar.dao.impl.AuthorDaoImpl_24133034;
import vn.iotstar.dao.impl.BookDaoImpl_24133034;
import vn.iotstar.model.Author_24133034;
import vn.iotstar.model.Book_24133034;
import vn.iotstar.service.IBookService_24133034;

import java.util.List;

public class BookServiceImpl_24133034 implements IBookService_24133034 {

    private final IBookDao_24133034 bookDao = new BookDaoImpl_24133034();
    private final IAuthorDao_24133034 authorDao = new AuthorDaoImpl_24133034();

    @Override
    public List<Book_24133034> findAll(int page, int pageSize) {
        List<Book_24133034> list = bookDao.findAll(page, pageSize);
        for (Book_24133034 book : list) {
            book.setAuthors(authorDao.findByBookId(book.getBookid()));
        }
        return list;
    }

    @Override
    public int countAll() {
        return bookDao.countAll();
    }

    @Override
    public Book_24133034 findById(int bookid) {
        Book_24133034 book = bookDao.findById(bookid);
        if (book != null) {
            book.setAuthors(authorDao.findByBookId(bookid));
        }
        return book;
    }

    @Override
    public boolean insert(Book_24133034 book, List<Integer> authorIds) {
        int bookId = bookDao.insert(book);
        if (bookId > 0) {
            if (authorIds != null && !authorIds.isEmpty()) {
                for (Integer aid : authorIds) {
                    authorDao.addAuthorToBook(bookId, aid);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Book_24133034 book, List<Integer> authorIds) {
        boolean updated = bookDao.update(book);
        if (updated && authorIds != null) {
            authorDao.removeAuthorsByBookId(book.getBookid());
            for (Integer aid : authorIds) {
                authorDao.addAuthorToBook(book.getBookid(), aid);
            }
        }
        return updated;
    }

    @Override
    public boolean delete(int bookid) {
        return bookDao.delete(bookid);
    }

    @Override
    public List<Book_24133034> findByAuthorId(int authorId, int page, int pageSize) {
        List<Book_24133034> books = bookDao.findByAuthorId(authorId, page, pageSize);
        for (Book_24133034 book : books) {
            book.setAuthors(authorDao.findByBookId(book.getBookid()));
        }
        return books;
    }

    @Override
    public int countByAuthorId(int authorId) {
        return bookDao.countByAuthorId(authorId);
    }
}
