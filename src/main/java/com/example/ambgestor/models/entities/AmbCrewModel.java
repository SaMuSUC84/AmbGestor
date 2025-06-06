package com.example.ambgestor.models.entities;

import com.example.ambgestor.models.daos.AmbUserDAO;

import javax.persistence.*;

@Entity
@Table(name = "dota_ag")
public class AmbCrewModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDOTA_DA", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CONDUCTDOTA_DA", nullable = false)
    private AmbUserModel conductdotaDa;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "SANITDOTA_DA", nullable = false)
    private AmbUserModel sanitdotaDa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FACULTDOTA_DA")
    private AmbUserModel facultdotaDa;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "UNITDOTA_DA", nullable = false)
    private AmbUnitModel unitdotaDa;

    //CONSTRUCTORES
    public AmbCrewModel() {}

    public AmbCrewModel(Integer id, AmbUserModel conductdotaDa,AmbUserModel sanitdotaDa,AmbUnitModel unitdotaDa) {
        this.id = id;
        this.conductdotaDa = conductdotaDa;
        this.sanitdotaDa = sanitdotaDa;
        this.unitdotaDa = unitdotaDa;
    }

    public AmbCrewModel (Integer id, AmbUserModel conductdotaDa, AmbUserModel sanitdotaDa, AmbUserModel doctorDota, AmbUnitModel unitdotaD){
        this.id = id;
        this.conductdotaDa = conductdotaDa;
        this.sanitdotaDa = sanitdotaDa;
        this.facultdotaDa = doctorDota;
        this.unitdotaDa = unitdotaDa;
    }

    // GETTER & SETTER
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AmbUserModel getConductDota() {
        return conductdotaDa;
    }

    public void setConductDota(AmbUserModel conductdotaDa) {
        this.conductdotaDa = conductdotaDa;
    }

    public AmbUserModel getSanitDota() {
        return sanitdotaDa;
    }

    public void setSanitDota(AmbUserModel sanitdotaDa) {
        this.sanitdotaDa = sanitdotaDa;
    }

    public AmbUserModel getDoctorDota() {
        return facultdotaDa;
    }

    public void setDoctorDota(AmbUserModel facultdotaDa) {
        this.facultdotaDa = facultdotaDa;
    }

    public AmbUnitModel getUnitDota() {
        return unitdotaDa;
    }

    public void setUnitDota(AmbUnitModel unitdotaDa) {
        this.unitdotaDa = unitdotaDa;
    }


    //METODO PARA VALIDAR SI HAY DOCTOR EN LA DOTACION PARA FORMATEO DE LA VISTA Y LA TABLA
    public String isDoctor(){

        return (getDoctorDota() != null && !getDoctorDota().getName().isBlank())
                ? getDoctorDota().getName()
                : "Sin facultativo";
    }

    // METODO TOSTRING
    public String toCrewString() {
        return "Dotación {" +
                "ID=" + id +
                ", conductor = " + conductdotaDa +
                ", sanitario = " + sanitdotaDa +
                ", facultativo = " + facultdotaDa +
                ", unidad = " + unitdotaDa +
                "' }'";
    }
}