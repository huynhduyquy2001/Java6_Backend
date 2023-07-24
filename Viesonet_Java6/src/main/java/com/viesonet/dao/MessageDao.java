package com.viesonet.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import com.viesonet.entity.Message;

public interface MessageDao extends JpaRepository<Message, String> {

}
