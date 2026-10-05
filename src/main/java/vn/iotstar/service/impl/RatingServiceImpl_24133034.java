package vn.iotstar.service.impl;

import vn.iotstar.dao.IRatingDao_24133034;
import vn.iotstar.dao.impl.RatingDaoImpl_24133034;
import vn.iotstar.model.Rating_24133034;
import vn.iotstar.service.IRatingService_24133034;

import java.util.List;

public class RatingServiceImpl_24133034 implements IRatingService_24133034 {

    private final IRatingDao_24133034 ratingDao = new RatingDaoImpl_24133034();

    @Override
    public List<Rating_24133034> findByBookId(int bookId) {
        return ratingDao.findByBookId(bookId);
    }

    @Override
    public int countByBookId(int bookId) {
        return ratingDao.countByBookId(bookId);
    }

    @Override
    public boolean addOrUpdateRating(Rating_24133034 rating) {
        return ratingDao.insert(rating);
    }
}
