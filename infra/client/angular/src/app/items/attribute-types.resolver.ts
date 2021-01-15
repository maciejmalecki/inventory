import {Resolve} from '@angular/router';
import {AttributeHeader, AttributeService} from '../shared/services/attribute.service';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttributeTypesResolver implements Resolve<Array<AttributeHeader>> {

  constructor(private readonly attributeTypeService: AttributeService) {
  }

  resolve(): Observable<Array<AttributeHeader>> {
    return this.attributeTypeService.getAllAttributes();
  }
}
