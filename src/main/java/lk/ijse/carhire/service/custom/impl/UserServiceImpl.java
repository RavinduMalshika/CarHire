package lk.ijse.carhire.service.custom.impl;

import lk.ijse.carhire.dao.DaoFactory;
import lk.ijse.carhire.dao.custom.UserDao;
import lk.ijse.carhire.dto.UserDto;
import lk.ijse.carhire.entity.UserEntity;
import lk.ijse.carhire.service.custom.UserService;

public class UserServiceImpl implements UserService {
    UserDao userDao = (UserDao) DaoFactory.getInstance().getDao(DaoFactory.DaoTypes.USER);
    @Override
    public UserDto getUser(String username) {
        UserEntity userEntity = userDao.get(username, session);
        if(userEntity != null) {
            return new UserDto(
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getName(),
                    userEntity.getEmail(),
                    userEntity.getMobile()
            );
        } else {
            return null;
        }
    }
}
