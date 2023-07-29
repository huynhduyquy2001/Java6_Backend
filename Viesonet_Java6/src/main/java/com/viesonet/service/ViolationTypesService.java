package com.viesonet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationTypesDao;
import com.viesonet.entity.ViolationTypes;

@Service
public class ViolationTypesService {
	@Autowired
	ViolationTypesDao violationTypesDao;
	public List<ViolationTypes> getViolations() {
		return violationTypesDao.findAll();
	}
	public ViolationTypes getById(int id) {
		Optional<ViolationTypes> obj = violationTypesDao.findById(id);
		return obj.orElse(null);
	}
}
