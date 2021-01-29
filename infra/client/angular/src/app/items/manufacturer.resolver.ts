import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {Manufacturer, ManufacturerService} from '../shared/services/manufacturer.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManufacturerResolver implements Resolve<Array<Manufacturer>> {

  constructor(private readonly manufacturerService: ManufacturerService) {
  }

  resolve(): Observable<Array<Manufacturer>> {
    return this.manufacturerService.getAllManufacturers();
  }
}
