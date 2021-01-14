import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface ItemClassHeader {
  name: string;
  description: string;
}

export interface ItemClassId {
  id: string;
  version: number;
}

export interface ItemClass {
  id: ItemClassId;
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

  getDraftItemClass(name: string): Observable<ItemClass> {
    return this.httpClient.get<ItemClass>(`${apiPrefix}/${name}/draft`);
  }

  newDraftItemClass(name: string): Observable<ItemClass> {
    return this.httpClient.put<ItemClass>(`${apiPrefix}/${name}/draft`, null);
  }

  updateDraftItemClass(
    name: string,
    description: string | null,
    unitCode: string | null,
    addedAttributes: Array<string>,
    removedAttributes: Array<string>): Observable<HttpResponse<any>> {
    return this.httpClient.post<ItemClass>(`${apiPrefix}/${name}/draft`, {
      description,
      unitCode,
      addedAttributes,
      removedAttributes
    }, {observe: 'response'});
  }

  completeDraftItemClass(name: string): Observable<ItemClass> {
    return this.httpClient.post<ItemClass>(`${apiPrefix}/${name}/draft/complete`, null);
  }

  rejectDraftItemClass(name: string): Observable<HttpResponse<any>> {
    return this.httpClient.delete(`${apiPrefix}/${name}/draft`, {observe: 'response'});
  }
}
