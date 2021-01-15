import {Resolve} from '@angular/router';
import {AttributeHeader, AttributeService} from '../shared/services/attribute.service';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttributesResolver implements Resolve<Array<AttributeHeader>> {

  constructor(private readonly attributeService: AttributeService) {
  }

  resolve(): Observable<Array<AttributeHeader>> {
    return this.attributeService.getAllAttributes();
  }
}
