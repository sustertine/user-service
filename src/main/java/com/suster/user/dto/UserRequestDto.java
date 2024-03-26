package com.suster.user.dto;


import com.suster.user.vao.User;
import io.smallrye.mutiny.Uni;
import lombok.Data;

@Data
public class UserRequestDto {
    private String username;
    private String email;
    private String password;

    public Uni<User> toEntity() {
        User user = new User();
        user.setUsername(this.getUsername());
        user.setEmail(this.getEmail());
        user.setPassword(this.getPassword());

        return Uni.createFrom().item(user);
    }
}
