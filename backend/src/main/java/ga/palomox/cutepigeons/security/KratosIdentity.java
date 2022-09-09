package ga.palomox.cutepigeons.security;

import java.util.UUID;

import ga.palomox.lightrest.rest.model.Identity;

public class KratosIdentity implements Identity<UUID>{
	private UUID id; 
	private String username; 
		
	public KratosIdentity(UUID id, String username){
		this.id = id;
		this.username = username; 
	}
	
	@Override
	public UUID getId() {
		return id; 
	}

	@Override
	public String getUsername() {
		return username; 
	}

}
