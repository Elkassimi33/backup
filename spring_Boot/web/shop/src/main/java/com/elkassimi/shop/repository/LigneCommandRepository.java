package com.elkassimi.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elkassimi.shop.model.LigneCommand;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneCommandRepository extends JpaRepository<LigneCommand, Long> {

    List<LigneCommand> findByCommandCommandId(Long commandId);


}
