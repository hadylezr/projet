package Services;


import Models.Reponse;
import Utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class reponseService implements iService<Reponse> {
    Connection conn ;

    public reponseService() {
        this.conn= MyDb.getInstance().getConn();
    }


    @Override
    public List<Reponse> display() throws Exception {
        List<Reponse> list = new ArrayList<>();
        String sql = "SELECT * FROM reponse";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            list.add(mapReponse(rs));
        }
        return list;
    }

    @Override
    public void create(Reponse r) throws Exception {
        // Vérifier si une réponse existe déjà pour ce congé
        if (existeReponse(r.getIdConge())) {
            // Si une réponse existe déjà, on retourne un message
            System.out.println("Réponse existe déjà pour le congé ID " + r.getIdConge());
        } else {
            // Sinon, insérer une nouvelle réponse
            String sql = "INSERT INTO reponseconge (id_conge, id_admin, decision, commentaire, date_decision) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, r.getIdConge());
            stmt.setInt(2, r.getIdAdmin());
            stmt.setString(3, r.getDecision());
            stmt.setString(4, r.getCommentaire());
            stmt.setTimestamp(5, r.getDateDecision());
            stmt.executeUpdate();
            System.out.println("Réponse ajoutée avec succès pour le congé ID " + r.getIdConge());
        }
    }

    // Méthode pour vérifier si une réponse existe déjà pour un congé
    private boolean existeReponse(int idConge) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reponseconge WHERE id_conge = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idConge);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;  // Retourne true si une réponse existe déjà
        }
        return false;
    }


    @Override
    public void update(Reponse r) throws Exception {
        String sql = "UPDATE reponse SET id_conge = ?, id_admin = ?, decision = ?, commentaire = ?, date_decision = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, r.getIdConge());
        stmt.setInt(2, r.getIdAdmin());
        stmt.setString(3, r.getDecision());
        stmt.setString(4, r.getCommentaire());
        stmt.setTimestamp(5, r.getDateDecision());
        stmt.setInt(6, r.getId());
        int rows = stmt.executeUpdate();
        if (rows == 0) throw new Exception("Aucune réponse trouvée avec l'ID " + r.getId());
        System.out.println("Réponse mise à jour.");
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM reponse WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int rows = stmt.executeUpdate();
        if (rows == 0) throw new Exception("Aucune réponse trouvée avec l'ID " + id);
        System.out.println("Réponse supprimée.");
    }

    // Utilitaire : convertir un ResultSet en objet Reponse
    private Reponse mapReponse(ResultSet rs) throws SQLException {
        Reponse r = new Reponse();
        r.setId(rs.getInt("id"));
        r.setIdConge(rs.getInt("id_conge"));
        r.setIdAdmin(rs.getInt("id_admin"));
        r.setDecision(rs.getString("decision"));
        r.setCommentaire(rs.getString("commentaire"));
        r.setDateDecision(rs.getTimestamp("date_decision"));
        return r;
    }
}
