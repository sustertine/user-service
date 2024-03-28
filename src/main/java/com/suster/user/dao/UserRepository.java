package com.suster.user.dao;

import com.suster.user.vao.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public Uni<User> findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
