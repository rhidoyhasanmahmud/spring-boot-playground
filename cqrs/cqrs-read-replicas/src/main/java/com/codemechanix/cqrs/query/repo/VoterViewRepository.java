package com.codemechanix.cqrs.query.repo;

import com.codemechanix.cqrs.query.model.VoterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoterViewRepository extends JpaRepository<VoterView, Integer> {

    @Query("""
    select v from VoterView v
    where lower(v.username) like lower(concat('%', :q, '%'))
       or lower(v.userNid)  like lower(concat('%', :q, '%'))
       or lower(coalesce(v.address,'')) like lower(concat('%', :q, '%'))
       or lower(coalesce(v.phone,''))   like lower(concat('%', :q, '%'))
    order by v.updatedAt desc
  """)
    List<VoterView> search(String q);

    List<VoterView> findTop10ByOrderByUpdatedAtDesc();
}
