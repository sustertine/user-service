package com.suster.user.vao;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "users")
public class User extends ReactivePanacheMongoEntity {
    private String username;
    private String email;
    private String password;
}
