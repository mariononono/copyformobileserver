package com.example.mjukvarukonstruktionbokningserver.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "adminsettings")
@EntityListeners(AuditingEntityListener.class)
public class AdminSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "maxhours")
    private int maxhours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxhours() {
        return maxhours;
    }

    public void setMaxhours(int maxhours) {
        this.maxhours = maxhours;
    }
}
