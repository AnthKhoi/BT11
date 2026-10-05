package vn.iotstar.dao;

import vn.iotstar.model.User_24133034;
import java.util.List;

public interface IUserDao_24133034 {
    boolean insert(User_24133034 user);
    boolean update(User_24133034 user);
    boolean updateOtp(String email, String otp);
    boolean activateAccount(String email);
    boolean updateLastLogin(int userId);
    User_24133034 findById(int id);
    User_24133034 findByEmail(String email);
    User_24133034 checkLogin(String email, String password);
    boolean checkExistEmail(String email);
    List<User_24133034> findAll();
}
