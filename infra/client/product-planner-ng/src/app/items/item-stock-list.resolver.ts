import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {ItemStockHeader, StockService} from '../shared/services/stock.service';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ItemStockListResolver implements Resolve<Array<ItemStockHeader>> {
  constructor(private readonly stockService: StockService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<Array<ItemStockHeader>> {
    return this.stockService.getStockList();
  }
}
