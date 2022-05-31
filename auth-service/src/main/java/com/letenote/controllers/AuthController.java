package com.letenote.controllers;

import com.letenote.services.JwtService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/auth")
public class AuthController {

    @Inject
    JwtService jwtService;

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(){
        String token = jwtService.generateToken();
        return Response.ok(token).build();
    }
}
