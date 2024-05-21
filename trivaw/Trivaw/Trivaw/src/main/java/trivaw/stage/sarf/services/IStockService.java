package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Account;
import trivaw.stage.sarf.Entities.Stock;

import java.util.List;

public interface IStockService {
    public List<Stock> getAllStock();
    Stock getStockById(Integer idStock);
    Stock createStock(Stock a);
    Stock updateStock(Integer idStock, Stock a);
    void deleteStock(Integer  idAcc);
    List<Stock> getStockByUser(Integer idUser);
    Stock createStockByUser(Integer idUser,Stock a);
    List<Stock> updateStockByUser(Integer idUser, Stock a, Integer idStock);
    List<String> getDistinctDevisesByUserId(Integer idUser) ;
}
