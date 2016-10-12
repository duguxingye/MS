package com.tys.ms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USER_ATTEMPT")
public class UserAttempts implements Serializable {

    private static final long serialVersionUID = -8457357237561702950L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="USERNAME", unique=true, nullable=false)
    private String username;

    @Column(name="ATTEMPT", nullable=false)
    private int attempts;

    @Column(name="LAST_MODIFIED", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
