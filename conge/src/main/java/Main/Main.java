package Main;

import Models.Conge;
import Models.Reponse;
import Services.congeService;
import Services.reponseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

    public static void main(String[] args) throws Exception {
        congeService congeSvc = new congeService();
        reponseService reponseSvc = new reponseService();

        // 1. Analyse des dates
        java.sql.Date dateDebut = parseDate("2024-01-06");
        java.sql.Date dateFin = parseDate("2024-01-05");

        if (dateDebut != null && dateFin != null) {
            // 2. Création du congé
            // createConge(congeSvc, 1, dateDebut, dateFin, "maladie", "justif.pdf", "en attente");

            // 3. Création d'une réponse pour ce congé
            createReponseConge(reponseSvc, 5, 1, "accepté", "Justification correcte");
        } else {
            System.out.println("Erreur lors de l’analyse des dates.");
        }
    }

    // 1. Méthode pour analyser les dates et les convertir en java.sql.Date
    private static java.sql.Date parseDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date utilDate = null;
        try {
            utilDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Erreur de format de date: " + dateStr);
            e.printStackTrace();
        }
        return utilDate != null ? new java.sql.Date(utilDate.getTime()) : null;
    }

//    // 2. Méthode pour créer un congé
//    private static void createConge(congeService service, int idUser, java.sql.Date dateDebut, java.sql.Date dateFin,
//                                    String typeConge, String justificatif, String statut) {
//        Conge conge = new Conge(idUser, dateDebut, dateFin, typeConge, justificatif, statut);
//        try {
//            service.create(conge);
//        } catch (Exception e) {
//            System.out.println("Erreur lors de la création du congé: " + e.getMessage());
//        }
//    }

    // 3. Méthode pour créer une réponse pour un congé
    private static void createReponseConge(reponseService reponseService, int idConge, int idAdmin, String decision, String commentaire) {
        Reponse reponse = new Reponse(idConge, idAdmin, decision, commentaire);
        try {
            reponseService.create(reponse);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la réponse: " + e.getMessage());
        }
    }
}
