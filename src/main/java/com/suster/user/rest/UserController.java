package com.suster.user.rest;

import com.suster.user.dao.UserRepository;
import com.suster.user.dto.UserRequestDto;
import com.suster.user.vao.User;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
        return userRequestDto.toEntity().onItem()
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
}
