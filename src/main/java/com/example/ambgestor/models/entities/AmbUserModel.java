package com.example.ambgestor.models.entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user_ag")
public class AmbUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSER_UA", nullable = false)
    private Integer id;

    @Column(name = "USEREMAIL_UA", nullable = false, length = 100)
    private String useremailUa;

    @Column(name = "PASSWORD_UA", nullable = false, length = 100)
    private String passwordUa;

    @Column(name = "USERNAME_UA", nullable = false, length = 50)
    private String usernameUa;

    @Column(name = "USERSURNAME_UA", nullable = false, length = 50)
    private String usersurnameUa;

    @Column(name = "USERPHONE_UA", nullable = false)
    private Integer userphoneUa;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "USERPROFE_UA", nullable = false)
    private AmbProfModel userprofeUa;

    @OneToMany(mappedBy = "conductdotaDa")
    private Set<AmbCrewModel> conductDota = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sanitdotaDa")
    private Set<AmbCrewModel> sanitDota = new LinkedHashSet<>();

    @OneToMany(mappedBy = "facultdotaDa")
    private Set<AmbCrewModel> doctorDota = new LinkedHashSet<>();

    //CONSTRUCTORES
    public AmbUserModel() {}

    public AmbUserModel(Integer id, String useremailUa, String passwordUa, String usernameUa, String usersurnameUa, Integer userphoneUa, AmbProfModel userprofeUa){
        this.id = id;
        this.useremailUa = useremailUa;
        this.passwordUa = passwordUa;
        this.usernameUa = usernameUa;
        this.usersurnameUa = usernameUa;
        this.userphoneUa = userphoneUa;
        this.userprofeUa = userprofeUa;
    }

    public AmbUserModel(String mail, String number, String test, String testeando, int i, AmbProfModel profession) {
        this.useremailUa = mail;
        this.passwordUa = number;
        this.usernameUa = test;
        this.usersurnameUa = testeando;
        this.userphoneUa = i;
        this.userprofeUa = profession;
    }


    // GETTER & SETTER
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return useremailUa;
    }

    public void setEmail(String useremailUa) {
        this.useremailUa = useremailUa;
    }

    public String getPassword() {
        return passwordUa;
    }

    public void setPassword(String passwordUa) {
        this.passwordUa = passwordUa;
    }

    public String getName() {
        return usernameUa;
    }

    public void setName(String usernameUa) {
        this.usernameUa = usernameUa;
    }

    public String getSurName() {
        return usersurnameUa;
    }

    public void setSurName(String usersurnameUa) {
        this.usersurnameUa = usersurnameUa;
    }

    public Integer getPhone() {
        return userphoneUa;
    }

    public void setPhone(Integer userphoneUa) {
        this.userphoneUa = userphoneUa;
    }

    public AmbProfModel getProfession() {
        return userprofeUa;
    }

    public void setProfession(AmbProfModel userprofeUa) {
        this.userprofeUa = userprofeUa;
    }

    public Set<AmbCrewModel> getConductDota() {
        return conductDota;
    }

    public void setConductDota(Set<AmbCrewModel> conductDota) {
        this.conductDota = conductDota;
    }

    public Set<AmbCrewModel> getSanitDota() {
        return sanitDota;
    }

    public void setSanitDota(Set<AmbCrewModel> sanitDota) {
        this.sanitDota = sanitDota;
    }

    public Set<AmbCrewModel> getDoctorDota() {
        return doctorDota;
    }

    public void setDoctorDota(Set<AmbCrewModel> doctorDota) {
        this.doctorDota = doctorDota;
    }


    // METODO TOSTRING
    public String toUserString() {
        return "Usuairo { " +
                "Nombre: '" + getName() + '\'' +
                ", Apellido: '" + getSurName() + '\'' +
                ", Email: '" + getEmail() + '\'' +
                ", Profesion: '" + getProfession().getProfName() +
                "' }";
    }
}