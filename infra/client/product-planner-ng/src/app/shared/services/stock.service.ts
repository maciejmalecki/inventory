import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

export interface Stock {
  amount: number;
}

export interface ItemStockHeader {
  name: string;
  manufacturersCode: string | null;
  amount: number;
  unitCode: string;
  unitName: string;
}

const apiPrefix = '/api/planner/stock';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  constructor(private readonly httpClient: HttpClient) {
  }

  getStock(name: string): Observable<Stock> {
    return this.httpClient.get<Stock>(`${apiPrefix}/${name}`);
  }

  getStockList(): Observable<Array<ItemStockHeader>> {
    return this.httpClient.get<Array<ItemStockHeader>>(`${apiPrefix}/items`);
  }

  replenish(name: string, amount: number): Observable<Stock> {
    return this.httpClient.post<Stock>(`${apiPrefix}/${name}`, {replenish: amount});
  }

  deduct(name: string, amount: number): Observable<Stock> {
    return this.httpClient.post<Stock>(`${apiPrefix}/${name}`, {deduct: amount});
  }
}
