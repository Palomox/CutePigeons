package ga.palomox.cutepigeons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import ga.palomox.cutepigeons.repository.EbeanConfiguration;
import ga.palomox.cutepigeons.rest.ApiController;
import ga.palomox.cutepigeons.rest.CorsMiddleware;
import ga.palomox.cutepigeons.security.KetoPermsManager;
import ga.palomox.cutepigeons.security.KratosIdManager;
import ga.palomox.cutepigeons.security.KratosIdentity;
import ga.palomox.cutepigeons.service.IPostService;
import ga.palomox.cutepigeons.service.PostServiceImpl;
import ga.palomox.lightrest.LightrestServer;
import ga.palomox.lightrest.middleware.Middleware;
import ga.palomox.lightrest.rest.RestControllerClass;

public class CutepigeonsBackendApplication {
	
	private KetoPermsManager permsManager;
	private KratosIdManager idManager;
	private EbeanConfiguration ebeanConfig;
	private IPostService pigeonService;
	
	
	public CutepigeonsBackendApplication(String oryUrl, String oryPat, String username, String password, String dbUrl, String driver, int port) {
		this.permsManager = new KetoPermsManager(oryUrl, oryPat);
		this.idManager = new KratosIdManager(oryUrl);
		this.ebeanConfig = new EbeanConfiguration(username, password, dbUrl, driver);
		
		this.ebeanConfig.configure();
		
		this.pigeonService = new PostServiceImpl(this.permsManager);
		
		Middleware[] middlewares = {new CorsMiddleware()};
		LightrestServer server = new LightrestServer(
				port, 
				new RestControllerClass<ApiController, KratosIdentity, UUID>(new ApiController(this.pigeonService, this.idManager), this.idManager , this.permsManager),
				middlewares);
		
	}
	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(new File(args[0])));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new CutepigeonsBackendApplication(properties.getProperty("ory_url"), 
				properties.getProperty("ory_pat"),
				properties.getProperty("db_user"),
				properties.getProperty("db_password"),
				properties.getProperty("db_url"),
				properties.getProperty("db_driver"),
				Integer.valueOf(properties.getProperty("port")));
	}
	public KetoPermsManager getPermsManager() {
		return permsManager;
	}
	public KratosIdManager getIdManager() {
		return idManager;
	}
}
