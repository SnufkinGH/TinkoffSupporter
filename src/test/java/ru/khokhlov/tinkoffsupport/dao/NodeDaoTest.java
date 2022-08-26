package ru.khokhlov.tinkoffsupport.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ru.khokhlov.tinkoffsupport.model.Branch;
import ru.khokhlov.tinkoffsupport.model.NewNode;
import ru.khokhlov.tinkoffsupport.model.Node;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeDaoTest {

    @Test
    void show() {
        NodeDao nodeDao = new NodeDao();
        Node node = nodeDao.show(1);

        assertEquals("С чем конкретно у вас возникла проблема?", node.getMessage());
    }


    @Test
    void add() {
        NodeDao nodeDao = new NodeDao();
        NewNode newNode = new NewNode(1, "С тако", "Не ешьте тако");

        nodeDao.add(newNode);

        Node node = nodeDao.show(1);
        List<Branch> branchesList = node.getBranches();
        boolean check = false;

        for(Branch branch: branchesList){
            if(branch.getAnswer().equals("С тако")){
                check = true;
                nodeDao.deleteBranch(branch.getBranchId());
                break;
            }
        }

        assertTrue(check);
    }

    @Test
    void update() {
        NodeDao nodeDao = new NodeDao();

        Node node = new Node(228, "Пепе", null);

        nodeDao.update(1, node);

        Node nodeCheck = nodeDao.show(1);
        String message = nodeCheck.getMessage();

        node.setMessage("С чем конкретно у вас возникла проблема?");
        nodeDao.update(1, node);

        assertEquals("Пепе", message);
    }
}