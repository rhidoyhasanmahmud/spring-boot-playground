package com.codemechanix.cqrs.controller;

import com.codemechanix.cqrs.command.model.Voter;
import com.codemechanix.cqrs.command.service.VoterCommandService;
import com.codemechanix.cqrs.query.model.VoterView;
import com.codemechanix.cqrs.query.service.VoterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voters")
@RequiredArgsConstructor
public class VoterController {
    private final VoterCommandService cmd;
    private final VoterQueryService qry;

    @PostMapping
    public Voter create(@RequestBody Voter v) { return cmd.create(v); }

    @PutMapping("/{id}")
    public Voter update(@PathVariable Integer id, @RequestBody Voter v) { return cmd.update(id, v); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { cmd.delete(id); }

    @GetMapping("/latest")
    public List<VoterView> latest() { return qry.latest(); }

    @GetMapping("/search")
    public List<VoterView> search(@RequestParam String q) { return qry.search(q); }
}
