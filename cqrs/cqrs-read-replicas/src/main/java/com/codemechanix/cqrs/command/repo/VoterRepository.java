package com.codemechanix.cqrs.command.repo;

import com.codemechanix.cqrs.command.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoterRepository extends JpaRepository<Voter, Integer> { }
