package com.codemechanix.spring_data.mapper;

import com.codemechanix.spring_data.dto.CreateCustomer;
import com.codemechanix.spring_data.dto.CustomerResponse;
import com.codemechanix.spring_data.dto.UpdateCustomer;
import com.codemechanix.spring_data.model.Customer;
import org.mapstruct.*;

/**
 * Handles mapping operations between Customer entity and its various representations
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    /**
     * Converts a Customer entity to its response representation
     */
    CustomerResponse entityToResponse(Customer customer);

    /**
     * Creates a new Customer entity from creation request
     */
    Customer createToEntity(CreateCustomer createCustomer);

    /**
     * Applies non-null updates from update request to an existing Customer entity
     * @param updateRequest The request containing updates
     * @param existingCustomer The entity to update
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void applyUpdates(UpdateCustomer updateRequest, @MappingTarget Customer existingCustomer);
}