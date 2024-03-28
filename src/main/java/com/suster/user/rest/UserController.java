package com.suster.user.rest;

import com.suster.user.dao.UserRepository;
import com.suster.user.dto.UserRequestDto;
import com.suster.user.dto.response.RegisterUserResponse;
import com.suster.user.vao.User;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/users")
public class UserController {
    @Inject
    private UserRepository userRepository;

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> registerUser(UserRequestDto userRequestDto) {
        return userRepository.findByEmail(userRequestDto.getEmail()).onItem().transformToUni(user -> {
            if (user != null) {
                return Uni.createFrom().item(Response.status(400).entity(new RegisterUserResponse("User with email already exists.")).build());
            } else {
                return userRequestDto.toEntityUni().onItem().transformToUni(userRepository::persistAndFlush)
                        .onItem().transform(user1 -> Response.status(201).entity(user1).build());
            }
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<User>> findAllUsers() {
        return userRepository.listAll();
    }

    @Path("/getByEmail/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getByEmail(@PathParam("email") String email) {
        return userRepository.findByEmail(email)
                .onItem().ifNotNull().transform(user -> Response.ok(user).build())
                .onItem().ifNull().continueWith(() -> Response.status(404).build());
    }
}
