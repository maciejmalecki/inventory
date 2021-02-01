import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

export interface Stock {
  amount: number;
}

const apiPrefix = '/api/stock';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  constructor(private readonly httpClient: HttpClient) {
  }

  getStock(name: string): Observable<Stock> {
    return this.httpClient.get<Stock>(`${apiPrefix}/${name}`);
  }

  replenish(name: string, amount: number): Observable<Stock> {
    return this.httpClient.post<Stock>(`${apiPrefix}/${name}`, {replenish: amount});
  }

  deduct(name: string, amount: number): Observable<Stock> {
    return this.httpClient.post<Stock>(`${apiPrefix}/${name}`, {deduct: amount});
  }
}
