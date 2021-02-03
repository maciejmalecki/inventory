import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {DictionaryItem, ItemClassId, UnitOfMeasurement} from './item-class.service';
import {Manufacturer} from './manufacturer.service';

export interface ItemHeader {
  name: string;
  itemClassName: string;
}

export type Value = ScalarValue | DictionaryValue;

export interface ScalarValue {
  name: string;
  unit: UnitOfMeasurement;
  value: number;
  scale: number;
}

export interface DictionaryValue {
  name: string;
  value: string;
  items: Array<DictionaryItem>;
}

export interface Item {
  id: string;
  name: string;
  manufacturer: Manufacturer | null;
  manufacturersCode: string | null;
  itemClassId: ItemClassId;
  values: Array<Value>;
}

export interface AttributeValuation {
  attribute: string;
  value: string;
}

export function isScalarValue(value: Value): value is ScalarValue {
  return (value as ScalarValue).unit !== undefined;
}

export function isDictionaryValue(value: Value): value is DictionaryValue {
  return (value as DictionaryValue).items !== undefined;
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

  updateItem(
    name: string,
    manufacturer: Manufacturer | null,
    manufacturersCode: string | null,
    attributeValuations: Array<AttributeValuation>): Observable<HttpResponse<string>> {
    return this.httpClient.post<string>(`${apiPrefix}/${name}`, {
      manufacturer,
      manufacturersCode,
      inValues: attributeValuations
    }, {observe: 'response'});
  }

  createItem(
    name: string,
    itemClassName: string,
    itemClassVersion: number,
    manufacturer: Manufacturer | null,
    manufacturersCode: string | null,
    attributeValuations: Array<AttributeValuation>): Observable<Item> {
    return this.httpClient.post<Item>(`${apiPrefix}`, {
      name,
      itemClassName,
      itemClassVersion,
      manufacturer,
      manufacturersCode,
      inValues: attributeValuations
    });
  }

  deleteItem(name: string): Observable<HttpResponse<any>> {
    return this.httpClient.delete(`${apiPrefix}/${name}`, {observe: 'response'});
  }
}
