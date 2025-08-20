package com.codemechanix.cqrs.read.api;

import com.codemechanix.cqrs.read.handler.CustomerQueryHandler;
import com.codemechanix.cqrs.read.view.CustomerView;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/queries/customers")
public class CustomerQueryController {
    private final CustomerQueryHandler handler;
    public CustomerQueryController(CustomerQueryHandler handler){ this.handler = handler; }

    @GetMapping("/{id}")
    public CustomerView get(@PathVariable Long id){ return handler.byId(id); }

    @GetMapping("/search")
    public List<CustomerView> search(@RequestParam String name){ return handler.search(name); }

    @GetMapping("/by-email")
    public CustomerView byEmail(@RequestParam String email){ return handler.byEmail(email); }
}
