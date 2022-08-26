package ru.khokhlov.tinkoffsupport.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.khokhlov.tinkoffsupport.model.Branch;
import ru.khokhlov.tinkoffsupport.model.NewNode;
import ru.khokhlov.tinkoffsupport.model.Node;

import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class NodeDao {

    @Value("${spring.datasource.url}")
    private static final String URL = "jdbc:postgresql://localhost:5432/akinator_db";
    @Value("${spring.datasource.username}")
    private static final String USERNAME = "postgres";
    @Value("${spring.datasource.password}")
    private static final String PASSWORD = "andrey163";

    private static Connection connection;

    public NodeDao() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Node show(int id){
        Node node = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM nodes LEFT JOIN branches " +
                    "ON nodes.node_id = branches.node_id WHERE nodes.node_id = ?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            node = new Node();
            node.setNodeId(id);
            node.setMessage(resultSet.getString("message"));

            List<Branch> branches = new ArrayList<>();

            do{
                branches.add(new Branch(resultSet.getInt("branch_id"),
                                        resultSet.getString("answer"),
                                        id,
                                        resultSet.getInt("next_node_id")));
            }while(resultSet.next());

            node.setBranches(branches);

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return node;
    }

    public List<Node> index(){
        List<Node> nodes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT node_id FROM nodes");

            while (resultSet.next()){
                int id = resultSet.getInt("node_id");
                nodes.add(show(id));
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nodes;
    }

    public void add(NewNode newNode){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO nodes(message) VALUES (?)");

            preparedStatement.setString(1, newNode.getNodeMessage());
            preparedStatement.executeUpdate();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT MAX(node_id) FROM nodes");
            resultSet.next();
            int nextNodeId = resultSet.getInt("max");

            statement.close();

            preparedStatement = connection.prepareStatement("INSERT INTO branches(answer, node_id, next_node_id) VALUES (?,?,?)");
            preparedStatement.setString(1, newNode.getEdgeMessage());
            preparedStatement.setInt(2, newNode.getPreviousNodeId());
            preparedStatement.setInt(3, nextNodeId);

            preparedStatement.executeUpdate();

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT next_node_id FROM nodes LEFT JOIN branches " +
                    "ON nodes.node_id = branches.node_id WHERE nodes.node_id = ?");

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            int i = 0;
            while(resultSet.next()) ++i;
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if(i > 1){
                do{
                    delete(resultSet.getInt("next_node_id"));
                }while(resultSet.next());
            }
            PreparedStatement preparedDeleteStatement = connection.prepareStatement("DELETE FROM nodes WHERE node_id = ?");
            preparedDeleteStatement.setInt(1, id);

            preparedDeleteStatement.executeUpdate();

            preparedDeleteStatement.close();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Node node){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE nodes SET message = ? WHERE node_id = ?");
            preparedStatement.setString(1, node.getMessage());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Branch showBranch(int id){
        Branch branch = new Branch();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM branches WHERE branch_id = ?");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            branch.setBranchId(resultSet.getInt("branch_id"));
            branch.setAnswer(resultSet.getString("answer"));
            branch.setNodeId(resultSet.getInt("node_id"));
            branch.setNextNodeId(resultSet.getInt("next_node_id"));

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branch;
    }

    public void updateBranch(int id, Branch branch){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE branches SET answer = ? WHERE branch_id = ?");
            preparedStatement.setString(1, branch.getAnswer());
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBranch(int id){
        delete(showBranch(id).getNextNodeId());
    }

    @PreDestroy
    public void close() {
        try {
            if(!connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
