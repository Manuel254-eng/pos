package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.Item;
import com.manu.springboot_backend.model.Role;
import com.manu.springboot_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemById(Long id);
    @Query("SELECT COALESCE(SUM(i.count), 0) FROM Item i WHERE i.name = :name AND i.deletedFlag != 'Y'")
    int countByName(@Param("name") String name);
    Optional<Item> findByName(String name);
    List<Item> findByDeletedFlagNot(String deletedFlag);



}
