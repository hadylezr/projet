package Models;


import java.sql.Timestamp;

public class Reponse {

    private int id;
    private int idConge;
    private int idAdmin;
    private String decision;      // "accepté" ou "refusé"
    private String commentaire;
    private Timestamp dateDecision;

    // Constructeurs
    public Reponse() {}

    public Reponse(int idConge, int idAdmin, String decision, String commentaire) {
        this.idConge = idConge;
        this.idAdmin = idAdmin;
        this.decision = decision;
        this.commentaire = commentaire;
        this.dateDecision = new Timestamp(System.currentTimeMillis());
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdConge() { return idConge; }
    public void setIdConge(int idConge) { this.idConge = idConge; }

    public int getIdAdmin() { return idAdmin; }
    public void setIdAdmin(int idAdmin) { this.idAdmin = idAdmin; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Timestamp getDateDecision() { return dateDecision; }
    public void setDateDecision(Timestamp dateDecision) { this.dateDecision = dateDecision; }

    // Affichage
    public void affiche() {
        System.out.println("ID: " + id);
        System.out.println("ID Conge: " + idConge);
        System.out.println("ID Admin: " + idAdmin);
        System.out.println("Décision: " + decision);
        System.out.println("Commentaire: " + commentaire);
        System.out.println("Date décision: " + dateDecision);
    }
}

