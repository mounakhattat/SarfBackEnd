package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.BureauDeChange;
import trivaw.stage.sarf.Entities.Stock;
import trivaw.stage.sarf.Entities.TauxDeChange;
import trivaw.stage.sarf.repository.BureauDeChangeRepository;
import trivaw.stage.sarf.repository.StockRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StockService implements IStockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private BureauDeChangeRepository bureauDeChangeRepository;

    @Override
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockById(Integer idAcc) {
        return stockRepository.findById(idAcc).orElse(null);
    }
    @Override
    public Stock findByDevise(String devise) {
        return stockRepository.findByDevise(devise)
                ;
    }

    public Stock findStockDeviseByUser(String devise,Integer idUser) {
        return stockRepository.findStockDeviseByUser(devise, idUser)
                ;
    }
    @Override
    public Stock createStock(Stock a) {
        return stockRepository.save(a);
    }

    @Override
    public Stock updateStock(Integer idStock, Stock a) {
        Stock existingStock = stockRepository.findById(idStock).orElse(null);
        if (existingStock != null) {

            return stockRepository.save(existingStock);

        } else {
            return null;
        }
    }

    @Override
    public void deleteStock(Integer idStock) {
        stockRepository.deleteById(idStock);
    }
    @Override
    public  List<Stock> getStockByUser(Integer idUser) {
        return    stockRepository.getStockByUser(idUser);
    }

    @Override
    public Stock createStockByUser(Integer idUser,Stock a) {
        BureauDeChange bureauDeChange=bureauDeChangeRepository.getBureauDeChangeByUser(idUser);
        Stock stock=new Stock();
        stock.setDevise(a.getDevise());
        stock.setQuantite(a.getQuantite());
        stock.setBureauDeChange(bureauDeChange);

        return stockRepository.save(stock);
    }
    @Override
    public List<Stock> updateStockByUser(Integer idUser, Stock a, Integer idStock) {
        List<Stock> stocks = stockRepository.getStockByUser(idUser);
        if (stocks != null && !stocks.isEmpty()) {
            for (Stock stock : stocks) {
                if (stock.getIdStock().equals(idStock)) {
                    stock.setDevise(a.getDevise());
                    stock.setQuantite(a.getQuantite());

                }
            }
            return stockRepository.saveAll(stocks);
        } else {
            return null;
        }
    }

@Override
    public List<Stock> getDistinctDevisesByUserId(Integer idUser) {
        return stockRepository.findDistinctDevisesByUserId(idUser);
    }
}

