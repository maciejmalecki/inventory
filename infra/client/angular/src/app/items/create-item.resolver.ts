import {DictionaryValue, Item, ScalarValue} from '../shared/services/item.service';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {
  Attribute,
  isDictionaryType,
  isScalarType,
  ItemClass,
  ItemClassService
} from '../shared/services/item-class.service';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CreateItemResolver implements Resolve<Item> {

  constructor(private readonly itemClassService: ItemClassService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Item> {
    function createValue(attribute: Attribute): ScalarValue | DictionaryValue {
      if (isScalarType(attribute.type)) {
        return {
          name: attribute.name,
          unit: attribute.type.unit,
          value: 0,
          scale: 1
        };
      } else if (isDictionaryType(attribute.type)) {
        return {
          name: attribute.name,
          value: '',
          items: attribute.type.items
        };
      }
    }
    function createBlankItem(itemClass: ItemClass): Item {
      return {
        id: '',
        name: '',
        itemClassId: itemClass.id,
        manufacturer: null,
        manufacturersCode: null,
        values: itemClass.attributes.map(value => createValue(value))
      };
    }
    return this.itemClassService.getItemClass(route.params.name).pipe(
      map(value => createBlankItem(value)));
  }

}
