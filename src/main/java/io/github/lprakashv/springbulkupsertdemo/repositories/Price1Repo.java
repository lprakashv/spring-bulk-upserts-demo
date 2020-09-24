package io.github.lprakashv.springbulkupsert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Price1Repo extends JpaRepository<Price1, PriceKey> {
    
}