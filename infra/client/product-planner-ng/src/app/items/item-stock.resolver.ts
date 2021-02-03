import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {Stock, StockService} from '../shared/services/stock.service';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ItemStockResolver implements Resolve<Stock> {

  constructor(private readonly stockService: StockService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<Stock> {
    return this.stockService.getStock(route.params.name);
  }
}
