package trivaw.stage.sarf.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import trivaw.stage.sarf.Entities.BureauDeChange;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BureauDeChangeServicesTest {
    @Autowired
    IBureauDeChangeServices bureauDeChangeServices;

    @Test
    void getAllBureauDeChange() {
        List<BureauDeChange> bureaux = bureauDeChangeServices.getAllBureauDeChange();
        assertNotNull(bureaux);
        assertEquals(2, bureaux.size());
    }

    @Test
    void getBureauDeChangeById() {
        BureauDeChange bureau = bureauDeChangeServices.getBureauDeChangeById(1);
        assertNotNull(bureau);
        assertEquals("BureauArbi", bureau.getNom());
    }

    @Test
    void deleteBureauDeChange() {
        bureauDeChangeServices.deleteBureauDeChange(1);
        BureauDeChange bureau = bureauDeChangeServices.getBureauDeChangeById(1);
        assertNull(bureau);
    }

    @Test
    void getBureauDeChangeByUser() {
        BureauDeChange bureau = bureauDeChangeServices.getBureauDeChangeByUser(1);
        assertNotNull(bureau);
        assertEquals("Bureau1", bureau.getNom());
    }
    @Test
    void createBureauDeChange() {
        BureauDeChange newBureau = new BureauDeChange();
        newBureau.setIdBureauDeChange(3);
        newBureau.setNom("Bureau3");

        BureauDeChange createdBureau = bureauDeChangeServices.createBureauDeChange(newBureau);

        assertNotNull(createdBureau);
        assertEquals("Bureau3", createdBureau.getNom());

        List<BureauDeChange> bureaux = bureauDeChangeServices.getAllBureauDeChange();
        assertEquals(3, bureaux.size());
    }
}