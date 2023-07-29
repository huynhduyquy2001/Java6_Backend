package com.viesonet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.InteractionDao;
import com.viesonet.entity.Interaction;

@Service
public class InteractionService {
	@Autowired
	InteractionDao interactionDao;

	public void plusInteraction(String interactingPerson, String interactedPerson) {
		Interaction interaction = interactionDao.findUserInteraction(interactingPerson, interactedPerson);
		Interaction interac = new Interaction();
		Date currentDate = new Date();
		if (interaction == null) {
			interac.setInteractedPerson(interactedPerson);
			interac.setInteractingPerson(interactingPerson);
			interac.setInteractionCount(1);
			interac.setMostRecentInteractionDate(currentDate);
			//lưu
			interactionDao.saveAndFlush(interac);
		}else {
			interaction.setMostRecentInteractionDate(currentDate);
			interaction.setInteractionCount(interaction.getInteractionCount() + 1);
			//lưu
			interactionDao.saveAndFlush(interaction);
		}
	}
	
	public void minusInteraction(String interactingPerson, String interactedPerson) {
		Interaction interaction = interactionDao.findUserInteraction(interactingPerson, interactedPerson);
		Date currentDate = new Date();
			interaction.setMostRecentInteractionDate(currentDate);
			interaction.setInteractionCount(interaction.getInteractionCount() - 1);
			//lưu
			interactionDao.saveAndFlush(interaction);
	}
	
	public List<Interaction> findListInteraction(String id){
		return interactionDao.findListInteraction(id);
	}
}
