import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

const apiPrefix = '/api/manufacturers';

export interface ManufacturerId {
  id: number;
}

export interface Manufacturer {
  id: ManufacturerId;
  name: string;
}

@Injectable({
  providedIn: 'root'
})
export class ManufacturerService {
  constructor(private readonly httpClient: HttpClient) {
  }

  getAllManufacturers(): Observable<Array<Manufacturer>> {
    return this.httpClient.get<Array<Manufacturer>>(apiPrefix);
  }
}
