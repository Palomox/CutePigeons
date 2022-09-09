package ga.palomox.cutepigeons.rest;

import org.eclipse.jetty.server.Request;

import ga.palomox.lightrest.middleware.Middleware;
import jakarta.servlet.http.HttpServletResponse;

public class CorsMiddleware implements Middleware {
	@Override
	public void doMiddleware(Request request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
}
