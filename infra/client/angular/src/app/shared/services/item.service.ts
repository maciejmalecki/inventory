import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

export interface ItemHeader {
  name: string;
  itemClassName: string;
}

const apiPrefix = '/api/items';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  constructor(private readonly httpClient: HttpClient) {
  }

  getAllItems(): Observable<Array<ItemHeader>> {
    return this.httpClient.get<Array<ItemHeader>>(apiPrefix);
  }
}
