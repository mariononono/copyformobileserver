package com.example.mjukvarukonstruktionbokningserver.repository;

import com.example.mjukvarukonstruktionbokningserver.model.TimeInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TimeIntervalRepository extends JpaRepository<TimeInterval, Integer> {

}