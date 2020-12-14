import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface ItemClassHeader {
  name: string;
  description: string;
}

export interface ItemClass {
  name: string;
  description: string;
  amountUnit: UnitOfMeasurement;
  attributes: Array<Attribute>;
}

export interface UnitOfMeasurement {
  code: string;
  name: string;
}

export interface Attribute {
  name: string;
  type: AttributeType;
}

export type AttributeType = ScalarType | DictionaryType;

export interface ScalarType {
  unit: UnitOfMeasurement;
}

export interface DictionaryType {
  items: Array<DictionaryItem>;
}

export interface DictionaryItem {
  code: string;
  value: string;
}

export function isScalarType(attributeType: AttributeType): attributeType is ScalarType {
  return (attributeType as ScalarType).unit !== undefined;
}

export function isDictionaryType(attributeType: AttributeType): attributeType is DictionaryType {
  return (attributeType as DictionaryType).items !== undefined;
}

const apiPrefix = '/api/itemClasses';

@Injectable({
  providedIn: 'root'
})
export class ItemClassService {

  constructor(private readonly httpClient: HttpClient) {
  }

  getAllItemClasses(): Observable<Array<ItemClassHeader>> {
    return this.httpClient.get<Array<ItemClassHeader>>(`${apiPrefix}`);
  }

  getItemClass(name: string): Observable<ItemClass> {
    return this.httpClient.get<ItemClass>(`${apiPrefix}/${name}`);
  }
}
