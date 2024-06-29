package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Stock;

import trivaw.stage.sarf.services.IStockService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/Stock")
public class StockController {
    @Autowired
    private IStockService stockService;


    @GetMapping("/retrieve-Stock")
    public List<Stock> getAllStock() {
        return stockService.getAllStock();
    }

    @GetMapping("/getStockById/{idStock}")
    public Stock getStockById(@PathVariable("idStock") Integer idStock) {
        return stockService.getStockById(idStock);
    }
    @GetMapping("/getStockByDevise/{devise}")
    public Stock getStockByDevise(@PathVariable("devise") String devise) {
        return stockService.findByDevise(devise);
    }

    @GetMapping("/getStockByUser/{idUser}")
    public List<Stock> getStockByUser(@PathVariable("idUser") Integer idUser){
        return stockService.getStockByUser(idUser);
    }
    @PostMapping("/createStock")
    public Stock createStock(@RequestBody Stock a) {
        return stockService.createStock(a);
    }
    @GetMapping("/getStockDevisesByUser/{idUser}")
    public List<Stock> getStockDevisesByUser(@PathVariable("idUser") Integer idUser) {
        return stockService.getDistinctDevisesByUserId(idUser);
    }

    @PutMapping("/update/idStock")
    public Stock updateStock(@PathVariable("idStock") Integer idStock, @RequestBody Stock a) {
        return stockService.updateStock(idStock, a);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStock(@PathVariable("id") Integer idStock) {
        stockService.deleteStock(idStock);
    }
    @PostMapping("/createStockByUser/{idUser}")
    public Stock createStockByUser(@PathVariable("idUser") Integer idUser, @RequestBody Stock a) {
        return stockService.createStockByUser(idUser,a);
    }
    @PutMapping("/updateStockByUser/{idUser}/{idStock}")
    public void updateStockByUser(@PathVariable("idUser") Integer idUser, @PathVariable("idStock") Integer idStock, @RequestBody Stock a) {
        stockService.updateStockByUser(idUser, a, idStock);
    }
}
