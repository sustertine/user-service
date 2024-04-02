package com.suster.user.dao;

import com.suster.user.vao.User;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements ReactivePanacheMongoRepository<User> {
    public Uni<User> findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public Uni<User> registerUser(User user) {
        return persist(user);
    }
}
