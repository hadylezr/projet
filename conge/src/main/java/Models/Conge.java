package Models;


import java.sql.Date;
import java.time.LocalDate;

public class Conge {


    private int idConge;

    private int idUser;

    private Date dateDebut;

    private Date dateFin;

    private String typeConge; // maladie, annuel, etc.

    private String justificatif; // lien fichier ou nom fichier

    private String statut; // validé, refusé, en attente

    // Constructeurs
   // public Conge(int idUser, Date date, String s, String maladie, String justificatif, String enAttente) {}

    public Conge(int idUser, Date dateDebut, Date dateFin,
                        String typeConge, String justificatif, String statut) {
        this.idUser = idUser;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeConge = typeConge;
        this.justificatif = justificatif;
        this.statut = statut;
    }

    public Conge() {

    }

    // Getters et setters
    public int getIdConge() { return idConge; }
    public void setIdConge(int id) { this.idConge = id; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public String getTypeConge() { return typeConge; }
    public void setTypeConge(String typeConge) { this.typeConge = typeConge; }

    public String getJustificatif() { return justificatif; }
    public void setJustificatif(String justificatif) { this.justificatif = justificatif; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public void affiche() {
        System.out.println("idConge: " + idConge);
        System.out.println("id_user: " + idUser);
        System.out.println("dateDebut: " + dateDebut);
        System.out.println("dateFin: " + dateFin);
        System.out.println("typeConge: " + typeConge);
        System.out.println("justificatif: " + justificatif);
        System.out.println("statut: " + statut);

    }
}
