package com.codemechanix.cqrs.write.api;

import com.codemechanix.cqrs.write.command.CreateCustomerCmd;
import com.codemechanix.cqrs.write.handler.CustomerCommandHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commands/customers")
public class CustomerCommandController {
    private final CustomerCommandHandler handler;
    public CustomerCommandController(CustomerCommandHandler handler){ this.handler = handler; }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid CreateCustomerCmd cmd){
        return ResponseEntity.ok(handler.handle(cmd));
    }
}