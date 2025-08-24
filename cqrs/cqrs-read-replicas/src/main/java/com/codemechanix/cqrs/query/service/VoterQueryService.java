package com.codemechanix.cqrs.query.service;

import com.codemechanix.cqrs.query.model.VoterView;
import com.codemechanix.cqrs.query.repo.VoterViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoterQueryService {
    private final VoterViewRepository repo;

    @Transactional(readOnly = true)
    public List<VoterView> latest() { return repo.findTop10ByOrderByUpdatedAtDesc(); }

    @Transactional(readOnly = true)
    public List<VoterView> search(String q) { return repo.search(q); }
}
