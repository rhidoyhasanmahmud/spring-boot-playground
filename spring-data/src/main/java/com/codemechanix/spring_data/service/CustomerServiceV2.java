package com.codemechanix.spring_data.service;

import com.codemechanix.spring_data.dto.CreateCustomer;
import com.codemechanix.spring_data.dto.CustomerResponse;
import com.codemechanix.spring_data.dto.UpdateCustomer;


public interface CustomerServiceV2 {
    CustomerResponse createCustomer(CreateCustomer createCustomer);
    CustomerResponse updateCustomer(Long id, UpdateCustomer updateCustomer);
}