package io.github.lprakashv.springbulkupsert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Price2Repo extends JpaRepository<Price2, Long> {

}