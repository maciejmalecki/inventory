import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Attribute, isDictionaryType, isScalarType, ItemClassId} from './item-class.service';

export interface ItemHeader {
  name: string;
  itemClassName: string;
}

export type Value = ScalarValue | DictionaryValue;

export interface ScalarValue {
  attribute: Attribute;
  data: number;
  scale: number;
}

export interface DictionaryValue {
  attribute: Attribute;
  data: string;
}

export interface Item {
  name: string;
  itemClassId: ItemClassId;
  values: Array<Value>;
}

export interface AttributeValuation {
  attribute: string;
  value: string;
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

  updateItem(name: string, attributeValuations: Array<AttributeValuation>): Observable<HttpResponse<string>> {
    return this.httpClient.post<string>(`${apiPrefix}/${name}`, attributeValuations, {observe: 'response'});
  }

  createItem(name: string, itemClassName: string, itemClassVersion: number, attributeValuations: Array<AttributeValuation>): Observable<Item> {
    return this.httpClient.post<Item>(`${apiPrefix}`, {name, itemClassName, itemClassVersion, inValues: attributeValuations});
  }

  deleteItem(name: string): Observable<HttpResponse<any>> {
    return this.httpClient.delete(`${apiPrefix}/${name}`, { observe: 'response'});
  }
}
