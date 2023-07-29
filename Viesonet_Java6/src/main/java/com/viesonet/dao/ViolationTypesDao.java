package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.ViolationTypes;

public interface ViolationTypesDao  extends JpaRepository<ViolationTypes, Integer>{
List<ViolationTypes> findAll();
}
