package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.RentDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface RentService extends SuperService {
    String saveRent(RentDto rentDto);

    String updateRent(RentDto rentDto);

    String deleteRent(String id);

    RentDto getRent(String id);

    List<RentDto> getAllRents();
}
