package trivaw.stage.sarf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import trivaw.stage.sarf.Entities.Enchere;
import trivaw.stage.sarf.Entities.Stock;
import trivaw.stage.sarf.Entities.TauxDeChange;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Integer> {
    @Query("SELECT b FROM Stock b WHERE b.bureauDeChange.user.idUser = :idUser")
    List<Stock> getStockByUser(@Param("idUser") Integer idUser);
   Stock  findByDevise(String Devise);
    @Query("SELECT b FROM Stock b WHERE b.devise = :devise AND b.bureauDeChange.user.idUser= :idUser")
    Stock findStockDeviseByUser(@Param("devise") String devise, @Param("idUser")Integer idUser);
    @Query("SELECT  s FROM Stock s WHERE s.bureauDeChange.user.idUser = :idUser")
    List<Stock> findDistinctDevisesByUserId(Integer idUser);
}
