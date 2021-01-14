import {Resolve} from '@angular/router';
import {AttributeTypeHeader, AttributeTypeService} from '../shared/services/attribute-type.service';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttributeTypesResolver implements Resolve<Array<AttributeTypeHeader>> {

  constructor(private readonly attributeTypeService: AttributeTypeService) {
  }

  resolve(): Observable<Array<AttributeTypeHeader>> {
    return this.attributeTypeService.getAllAttributeTypes();
  }
}
