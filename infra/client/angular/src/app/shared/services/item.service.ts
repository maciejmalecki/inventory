import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ItemClass} from './item-class.service';

export interface ItemHeader {
  name: string;
  itemClassName: string;
}

export interface Item {
  name: string;
  itemClass: ItemClass;
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

  getItem(name: string): Observable<Item> {
    return this.httpClient.get<Item>(`${apiPrefix}/${name}`);
  }
}
