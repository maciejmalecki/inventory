import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

export interface AttributeTypeHeader {
  name: string;
  scalar: boolean;
}

const apiPrefix = '/api/attributeTypes';

@Injectable({
  providedIn: 'root'
})
export class AttributeTypeService {

  constructor(private readonly httpClient: HttpClient) {
  }

  getAllAttributeTypes(): Observable<Array<AttributeTypeHeader>> {
    return this.httpClient.get<Array<AttributeTypeHeader>>(`${apiPrefix}`);
  }
}
