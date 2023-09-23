package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.UserDto;
import lk.ijse.carhire.service.SuperService;

public interface UserService extends SuperService {
    UserDto getUser(String username);
}
