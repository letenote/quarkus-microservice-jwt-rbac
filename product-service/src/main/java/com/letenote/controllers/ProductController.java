package com.letenote.controllers;

import com.letenote.models.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/products")
public class ProductController {
    List<Product> items = new ArrayList<>();

    @GET
    public Response getItems(){
        return Response.ok(items).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItem(Product newProduct){
        items.add(newProduct);
        return Response.ok(newProduct).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") String id){
        items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .ifPresent(item -> items.remove(item));

        return Response.noContent().build();
    }
}
