import {Item, ItemService} from '../../shared/services/item.service';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ItemResolver implements Resolve<Item> {

  constructor(private readonly itemService: ItemService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Item> {
    return this.itemService.getItem(route.params.name);
  }
}
