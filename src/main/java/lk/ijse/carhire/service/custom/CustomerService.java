package lk.ijse.carhire.service.custom;

import lk.ijse.carhire.dto.CustomerDto;
import lk.ijse.carhire.service.SuperService;

import java.util.List;

public interface CustomerService extends SuperService {
    String saveCustomer(CustomerDto customerDto);

    String updateCustomer(CustomerDto customerDto);

    String deleteCustomer(String id);

    CustomerDto getCustomer(String id);

    List<CustomerDto> getAllCustomers();
}
