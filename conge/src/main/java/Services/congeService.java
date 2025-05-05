package Services;

import Models.Conge;
import Utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class congeService implements iService<Conge> {
 Connection conn ;

 public congeService() {
     this.conn= MyDb.getInstance().getConn();
 }

    @Override
    public List<Conge> display() throws Exception {
        String req="select * from conge";
        PreparedStatement stmt=conn.prepareStatement(req);
        ResultSet rs =stmt.executeQuery();
        List<Conge> listConge=new ArrayList<>();
        while(rs.next()){
            Conge c1 = new Conge();
            c1.setIdConge(rs.getInt("id"));
            c1.setIdUser(rs.getInt("id_u"));
            c1.setDateDebut(rs.getDate("date"));
            c1.setDateFin(rs.getDate("date"));
            c1.setTypeConge(rs.getString("type"));
            c1.setJustificatif(rs.getString("justificatif"));
            c1.setStatut(rs.getString("status"));
            listConge.add(c1);
        }
        return listConge;
    }

    @Override
   public void create(Conge conge) throws Exception {
        // Vérifier si un congé existe déjà pour cet utilisateur sur cette période
        if (existeCongeChevauche(conge.getIdUser(), conge.getDateDebut(), conge.getDateFin(), -1)) {
            throw new Exception("Un congé existe déjà pour cet utilisateur sur cette période");
        }

        // Si aucun chevauchement, procéder à l'insertion
        String req = "insert into conge(id_user, date_debut, date_fin, type_conge, justificatif, statut) VALUES ('"
                + conge.getIdUser() + "', '"
                + conge.getDateDebut() + "', '"
                + conge.getDateFin() + "', '"
                + conge.getTypeConge() + "', '"
                + conge.getJustificatif() + "', '"
                + conge.getStatut() + "')";

        Statement stmt = conn.createStatement();
        stmt.execute(req);
    }

    /**
     * Vérifie si un congé existe déjà pour l'utilisateur sur la période spécifiée.
     * @param idUser - ID de l'utilisateur
     * @param dateDebut - Date de début du congé
     * @param dateFin - Date de fin du congé
     * @param idCongeExclu - ID du congé à exclure de la vérification (pour mise à jour), ou -1 si création
     * @return true si un chevauchement existe, false sinon
     */
    private boolean existeCongeChevauche(int idUser, java.sql.Date dateDebut, java.sql.Date dateFin, int idCongeExclu) {
        try {
            String req;
            PreparedStatement pstmt;

            // Construire la requête en fonction du cas (création ou mise à jour)
            if (idCongeExclu == -1) {
                // Cas création : pas besoin d'exclure un congé
                req = "SELECT COUNT(*) FROM conge WHERE id_user = ? AND "
                        + "((date_debut <= ? AND date_fin >= ?) OR "
                        + "(date_debut >= ? AND date_debut <= ?) OR "
                        + "(date_fin >= ? AND date_fin <= ?))";

                pstmt = conn.prepareStatement(req);
                pstmt.setInt(1, idUser);
                pstmt.setDate(2, dateDebut);
                pstmt.setDate(3, dateFin);
                pstmt.setDate(4, dateDebut);
                pstmt.setDate(5, dateFin);
                pstmt.setDate(6, dateDebut);
                pstmt.setDate(7, dateFin);
            } else {
                // Cas mise à jour : exclure le congé en cours de modification
                req = "SELECT COUNT(*) FROM conge WHERE id_user = ? AND id_conge != ? AND "
                        + "((date_debut <= ? AND date_fin >= ?) OR "
                        + "(date_debut >= ? AND date_debut <= ?) OR "
                        + "(date_fin >= ? AND date_fin <= ?))";

                pstmt = conn.prepareStatement(req);
                pstmt.setInt(1, idUser);
                pstmt.setInt(2, idCongeExclu);
                pstmt.setDate(3, dateDebut);
                pstmt.setDate(4, dateFin);
                pstmt.setDate(5, dateDebut);
                pstmt.setDate(6, dateFin);
                pstmt.setDate(7, dateDebut);
                pstmt.setDate(8, dateFin);
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void update(Conge conge) throws Exception {
        // Vérifier si un autre congé existe déjà pour cet utilisateur sur cette période
        // (on exclut le congé actuel de la vérification)
        if (existeCongeChevauche(conge.getIdUser(), conge.getDateDebut(), conge.getDateFin(), conge.getIdConge())) {
            throw new Exception("Un autre congé existe déjà pour cet utilisateur sur cette période");
        }

        // Correction de la requête SQL : on a besoin de la clause WHERE
        // Et il y avait un doublon de id_user dans la requête originale
        String req = "UPDATE conge SET id_user = ?, date_debut = ?, date_fin = ?, type_conge = ?, justificatif = ?, "
                + "statut = ? WHERE id_conge = ? AND statut = 'en attente'";

        PreparedStatement pstmt = conn.prepareStatement(req);
        pstmt.setInt(1, conge.getIdUser());
        pstmt.setDate(2, conge.getDateDebut());
        pstmt.setDate(3, conge.getDateFin());
        pstmt.setString(4, conge.getTypeConge());
        pstmt.setString(5, conge.getJustificatif());
        pstmt.setString(6, conge.getStatut());
        pstmt.setInt(7, conge.getIdConge());

        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new Exception("Le congé n'existe pas ou n'est pas en attente (ID " + conge.getIdConge() + ")");
        } else {
            // Si la mise à jour est réussie, afficher un message de succès
            System.out.println("Mise à jour réussie du congé avec ID " + conge.getIdConge());
        }
    }

    @Override
    public void delete(int id_conge) throws Exception {
            // Supprimer le congé uniquement s'il est en attente
            String req = "DELETE FROM conge WHERE id_conge = ? AND statut = 'en attente'";

            PreparedStatement pstmt = conn.prepareStatement(req);
            pstmt.setInt(1, id_conge);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Aucun congé en attente trouvé avec l'ID " + id_conge);
            } else {
                System.out.println("Le congé en attente avec l'ID " + id_conge + " a été supprimé.");
            }
        }

}

