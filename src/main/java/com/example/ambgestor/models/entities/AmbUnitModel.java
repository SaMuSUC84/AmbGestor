package com.example.ambgestor.models.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "unit_ag")
public class AmbUnitModel {

    @Id
    @Column(name = "UNITCODE_UTA", nullable = false)
    private Integer unitcodeUta;

    @Column(name = "UNITNAME_UTA", nullable = false)
    private String unitnameUta;

    @OneToMany(mappedBy = "unitdotaDa")
    private Set<AmbCrewModel> dotaAgs = new LinkedHashSet<>();

    //CONSTRUCTORES
    public AmbUnitModel() {}

    public AmbUnitModel(Integer unitcodeUta, String unitnameUta) {
        this.unitcodeUta = unitcodeUta;
        this.unitnameUta = unitnameUta;
    }

    // GETTER & SETTER
    public Integer getUnitCode() {
        return unitcodeUta;
    }

    public void setUnitCode(Integer unitcodeUta) {
        this.unitcodeUta = unitcodeUta;
    }

    public String getUnitName() {
        return unitnameUta;
    }

    public void setUnitName(String unitnameUta) {
        this.unitnameUta = unitnameUta;
    }

    public Set<AmbCrewModel> getUnitDota() {
        return dotaAgs;
    }

    public void setUnitDota(Set<AmbCrewModel> dotaAgs) {
        this.dotaAgs = dotaAgs;
    }

    // METODO TOSTRING
    public String toUnitString() {
        return "Unidad {" +
                "indicativo = " + unitcodeUta +
                ", recurso =' " + unitnameUta + '\'' +
                "' }'";
    }
}