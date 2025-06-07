package com.example.ambgestor.models.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * @author Samuel Alonso Viera
 */
@Entity
@Table(name = "prof_ag")
public class AmbProfModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPROF_PA", nullable = false)
    private Integer id;

    @Column(name = "PROFNAME_PA", nullable = false)
    private String profnamePa;

    @Column(name = "PROFCODE_PA", nullable = false, length = 50)
    private String profcodePa;

    @OneToMany(mappedBy = "userprofeUa")
    private Set<AmbUserModel> userAgs = new LinkedHashSet<>();

    //CONSTRUCTORES
    public AmbProfModel() {}

    public AmbProfModel(Integer id, String profnamePa , String profcodePa) {
        this.id = id;
        this.profnamePa = profnamePa;
        this.profcodePa = profcodePa;
    }

    // GETTERS & SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfName() {
        return profnamePa;
    }

    public void setProfName(String profnamePa) {
        this.profnamePa = profnamePa;
    }

    public String getProfCode() {
        return profcodePa;
    }

    public void setProfCode(String profcodePa) {
        this.profcodePa = profcodePa;
    }

    public Set<AmbUserModel> getUserProf() {
        return userAgs;
    }

    public void setUserProf(Set<AmbUserModel> userAgs) {
        this.userAgs = userAgs;
    }

    // METODO TOSTRING
    public String toProfString() {
        return "Profesión {" +
                "ID =" + id +
                ", nombre = '" + profnamePa + '\'' +
                ", código = '" + profcodePa + '\'' +
                "' }'";
    }
}