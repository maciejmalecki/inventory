import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

export interface AttributeHeader {
  name: string;
  scalar: boolean;
  unitCode: string | null;
  unitName: string | null;
}

const apiPrefix = '/api/planner/attributes';

@Injectable({
  providedIn: 'root'
})
export class AttributeService {

  constructor(private readonly httpClient: HttpClient) {
  }

  getAllAttributes(): Observable<Array<AttributeHeader>> {
    return this.httpClient.get<Array<AttributeHeader>>(`${apiPrefix}`);
  }
}
