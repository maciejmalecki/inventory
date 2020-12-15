import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Attribute, isDictionaryType, isScalarType, ItemClass} from './item-class.service';

export interface ItemHeader {
  name: string;
  itemClassName: string;
}

export type Value = ScalarValue | DictionaryValue;

export interface ScalarValue {
  attribute: Attribute;
  value: number;
  scale: number;
}

export interface DictionaryValue {
  attribute: Attribute;
  value: string;
}

export interface Item {
  name: string;
  itemClass: ItemClass;
  values: Array<Value>;
}

export function isScalarValue(value: Value): value is ScalarValue {
  return isScalarType(value.attribute.type);
}
export function isDictionaryValue(value: Value): value is DictionaryValue {
  return isDictionaryType(value.attribute.type);
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
