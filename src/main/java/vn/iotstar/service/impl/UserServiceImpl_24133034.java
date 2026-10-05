package vn.iotstar.service.impl;

import vn.iotstar.dao.IUserDao_24133034;
import vn.iotstar.dao.impl.UserDaoImpl_24133034;
import vn.iotstar.model.User_24133034;
import vn.iotstar.service.IUserService_24133034;
import vn.iotstar.util.EmailUtil_24133034;

import java.util.List;

public class UserServiceImpl_24133034 implements IUserService_24133034 {

    private final IUserDao_24133034 userDao = new UserDaoImpl_24133034();

    @Override
    public boolean register(User_24133034 user) {
        if (userDao.checkExistEmail(user.getEmail())) {
            return false;
        }
        // Sinh mã OTP và gán cho user
        String otp = EmailUtil_24133034.generateOtp();
        user.setOtp_code(otp);
        user.setIs_active(false); // Chưa kích hoạt
        
        boolean inserted = userDao.insert(user);
        if (inserted) {
            // Gửi email OTP
            EmailUtil_24133034.sendOtpEmail(user.getEmail(), otp);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User_24133034 user = userDao.findByEmail(email);
        if (user != null && otp != null && otp.trim().equals(user.getOtp_code())) {
            return userDao.activateAccount(email);
        }
        return false;
    }

    @Override
    public boolean resendOtp(String email) {
        User_24133034 user = userDao.findByEmail(email);
        if (user != null && !user.isIs_active()) {
            String otp = EmailUtil_24133034.generateOtp();
            userDao.updateOtp(email, otp);
            EmailUtil_24133034.sendOtpEmail(email, otp);
            return true;
        }
        return false;
    }

    @Override
    public User_24133034 login(String email, String password) {
        User_24133034 user = userDao.checkLogin(email, password);
        if (user != null && user.isIs_active()) {
            userDao.updateLastLogin(user.getId());
            return user;
        }
        return null;
    }

    @Override
    public User_24133034 findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User_24133034 findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public List<User_24133034> findAll() {
        return userDao.findAll();
    }
}
