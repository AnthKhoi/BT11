package vn.iotstar.dao;

import vn.iotstar.model.Rating_24133034;
import java.util.List;

public interface IRatingDao_24133034 {
    List<Rating_24133034> findByBookId(int bookId);
    int countByBookId(int bookId);
    boolean insert(Rating_24133034 rating);
    boolean checkUserRated(int userId, int bookId);
}
