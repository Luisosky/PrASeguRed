package co.edu.uniquindio.prasegured.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "subscribers")
public class Subscriber {
    @Id
    private String id;
    private String email;
    private LocalDateTime subscribedAt;
    private boolean active;

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public LocalDateTime getSubscribedAt() {return subscribedAt;}

    public void setSubscribedAt(LocalDateTime subscribedAt) {this.subscribedAt = subscribedAt;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}
}