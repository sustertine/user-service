package com.suster.user.rest;

import com.suster.user.dao.UserRepository;
import com.suster.user.dto.UserRequestDto;
import com.suster.user.vao.User;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;

import java.awt.*;
import java.util.List;

@Path("/api/users")
public class UserController {
    @Inject
    private UserRepository userRepository;

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<User>> registerUser(UserRequestDto userRequestDto) {
        return userRequestDto.toEntityUni().onItem()
                .transformToUni(user -> {
                    // TODO: Validation and error handling

                    return userRepository.persistAndFlush(user);
                }
                )
                .map(persisted -> RestResponse.ok());

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
