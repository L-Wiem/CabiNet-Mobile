package com.pfe.cabinet;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by JamilaHassine on 5/12/2016.
 */

public class MessagePatient implements Serializable {

    //déclaration des attributs de la class

    @SerializedName("id")
    public int id;

    @SerializedName("index")
    public String index;

    @SerializedName("nom")
    public String nom;

    @SerializedName("prenom")
    public String prenom;

    @SerializedName("date_naissance")
    public String date_naissance;

    @SerializedName("date_rdv")
    public String date_rdv;

    @SerializedName("heure_rdv")
    public String heure_rdv;
    @SerializedName("preferences")
    public String preferences;





}
