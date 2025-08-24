package com.codemechanix.cqrs.command.service;

import com.codemechanix.cqrs.command.model.Voter;
import com.codemechanix.cqrs.command.repo.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoterCommandService {
    private final VoterRepository repo;

    @Transactional
    public Voter create(Voter v) { return repo.save(v); }

    @Transactional
    public Voter update(Integer id, Voter payload) {
        var v = repo.findById(id).orElseThrow();
        v.setUsername(payload.getUsername());
        v.setUserNid(payload.getUserNid());
        v.setAddress(payload.getAddress());
        v.setPhone(payload.getPhone());
        v.setDob(payload.getDob());
        return repo.save(v);
    }

    @Transactional
    public void delete(Integer id) { repo.deleteById(id); }
}
