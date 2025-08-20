package com.codemechanix.spring_data.service;

import com.codemechanix.spring_data.dto.CreateCustomer;
import com.codemechanix.spring_data.dto.CustomerResponse;
import com.codemechanix.spring_data.dto.UpdateCustomer;
import com.codemechanix.spring_data.mapper.CustomerMapper;
import com.codemechanix.spring_data.model.Customer;
import com.codemechanix.spring_data.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplV2 implements CustomerServiceV2 {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerResponse createCustomer(CreateCustomer createCustomer) {
        Customer customer = customerMapper.createToEntity(createCustomer);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.entityToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, UpdateCustomer updateCustomer) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerMapper.applyUpdates(updateCustomer, customer);
        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.entityToResponse(updatedCustomer);
    }
}