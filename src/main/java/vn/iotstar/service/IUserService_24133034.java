package vn.iotstar.service;

import vn.iotstar.model.User_24133034;
import java.util.List;

public interface IUserService_24133034 {
    boolean register(User_24133034 user);
    boolean verifyOtp(String email, String otp);
    boolean resendOtp(String email);
    User_24133034 login(String email, String password);
    User_24133034 findById(int id);
    User_24133034 findByEmail(String email);
    boolean checkExistEmail(String email);
    List<User_24133034> findAll();
}
