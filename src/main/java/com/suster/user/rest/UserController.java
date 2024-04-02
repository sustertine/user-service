package com.suster.user.rest;

import com.suster.user.dao.UserRepository;
import com.suster.user.dto.UserRequestDto;
import com.suster.user.dto.response.RegisterUserResponse;
import com.suster.user.logging.LoggingService;
import com.suster.user.vao.User;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/api/users")
public class UserController {
    @Inject
    private UserRepository userRepository;

    @Inject
    private LoggingService loggingService;

    @Path("/register")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> registerUser(UserRequestDto userRequestDto) {
        loggingService.logInfo("POST", "/api/users/register", userRequestDto, "Registering user: " + userRequestDto);
        return userRepository.findByEmail(userRequestDto.getEmail()).onItem().transformToUni(user -> {
            if (user != null) {
                loggingService.logWarn("POST", "/api/users/register", userRequestDto, "User with email already exists: " + userRequestDto.getEmail());
                return Uni.createFrom().item(Response.status(400).entity(new RegisterUserResponse("User with email already exists.")).build());
            } else {
                return userRequestDto.toEntityUni().onItem().transformToUni(userRepository::registerUser)
                        .onItem().transform(user1 -> Response.status(201).entity(user1).build());
            }
        });
    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(UserRequestDto userRequestDto) {
        loggingService.logInfo("POST", "/api/users/login", userRequestDto, "Logging in user: " + userRequestDto);
        return userRepository.findByEmail(userRequestDto.getEmail()).onItem().transformToUni(user -> {
            if (user == null) {
                loggingService.logWarn("POST", "/api/users/login", userRequestDto, "User with email not found: " + userRequestDto.getEmail());
                return Uni.createFrom().item(Response.status(404).entity(new RegisterUserResponse("Invalid credentials.")).build());
            } else {
                if (user.getPassword().equals(userRequestDto.getPassword())) {
                    loggingService.logInfo("POST", "/api/users/login", userRequestDto, "Login successful for user: " + userRequestDto.getEmail());
                    return Uni.createFrom().item(Response.status(200).entity(new RegisterUserResponse("Login successful.")).build());
                } else {
                    loggingService.logWarn("POST", "/api/users/login", userRequestDto, "Invalid credentials for user: " + userRequestDto.getEmail());
                    return Uni.createFrom().item(Response.status(400).entity(new RegisterUserResponse("Invalid credentials.")).build());
                }
            }
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<User>> findAllUsers() {
        loggingService.logInfo("GET", "/api/users", null, "Fetching all users");
        return userRepository.listAll();
    }

    @Path("/get-by-email/{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getByEmail(@PathParam("email") String email) {
        loggingService.logInfo("GET", "/api/users/get-by-email/" + email, null, "Fetching user by email: " + email);
        return userRepository.findByEmail(email)
                .onItem().ifNotNull().transform(user -> Response.ok(user).build())
                .onItem().ifNull().continueWith(() -> Response.status(404).build());
    }

    @Path("/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateUser(@PathParam("id") String id, UserRequestDto userRequestDto) {
        loggingService.logInfo("PUT", "/api/users/" + id, userRequestDto, "Updating user with id: " + id);
        return userRepository.findById(new ObjectId(id)).onItem().transformToUni(user -> {
            if (user == null) {
                loggingService.logWarn("PUT", "/api/users/" + id, userRequestDto, "User with id not found: " + id);
                return Uni.createFrom().item(Response.status(404).build());
            } else {
                user.setUsername(userRequestDto.getUsername());
                user.setEmail(userRequestDto.getEmail());
                user.setPassword(userRequestDto.getPassword());
                return userRepository.persistOrUpdate(user).onItem().transform(user1 -> Response.ok(user1).build());
            }
        });
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteUser(@PathParam("id") String id) {
        loggingService.logInfo("DELETE", "/api/users/" + id, null, "Deleting user with id: " + id);
        return userRepository.findById(new ObjectId(id)).onItem().transformToUni(user -> {
            if (user == null) {
                loggingService.logWarn("DELETE", "/api/users/" + id, null, "User with id not found: " + id);
                return Uni.createFrom().item(Response.status(404).build());
            } else {
                return userRepository.delete(user).onItem().transform(user1 -> Response.ok().build());
            }
        });
    }
}