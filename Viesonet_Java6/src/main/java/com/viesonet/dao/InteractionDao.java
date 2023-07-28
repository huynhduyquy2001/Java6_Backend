package com.viesonet.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.viesonet.entity.Interaction;

public interface InteractionDao extends JpaRepository<Interaction, String> {
	@Query("SELECT i FROM Interaction i WHERE i.interactingPerson =:id and i.interactedPerson=:id2")
	Interaction findUserInteraction(String id, String id2);
	
	@Query("SELECT i FROM Interaction i WHERE i.interactedPerson =:interactedPerson")
	List<Interaction> findListInteraction(String interactedPerson);
}
