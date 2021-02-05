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

export type Attribute = ScalarAttribute | DictionaryAttribute;

export interface ScalarAttribute {
  name: string;
  unit: UnitOfMeasurement;
}

export interface DictionaryAttribute {
  name: string;
  items: Array<DictionaryItem>;
}

export interface DictionaryItem {
  code: string;
  value: string;
}

export function isScalarAttribute(attribute: Attribute): attribute is ScalarAttribute {
  return (attribute as ScalarAttribute).unit !== undefined;
}

export function isDictionaryAttribute(attribute: Attribute): attribute is DictionaryAttribute {
  return (attribute as DictionaryAttribute).items !== undefined;
}

const apiPrefix = '/api/planner/itemClasses';

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
