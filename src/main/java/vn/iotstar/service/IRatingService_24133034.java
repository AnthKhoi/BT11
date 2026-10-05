package vn.iotstar.service;

import vn.iotstar.model.Rating_24133034;
import java.util.List;

public interface IRatingService_24133034 {
    List<Rating_24133034> findByBookId(int bookId);
    int countByBookId(int bookId);
    boolean addOrUpdateRating(Rating_24133034 rating);
}
