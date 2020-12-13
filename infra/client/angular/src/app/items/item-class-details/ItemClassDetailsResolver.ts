import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {ItemClass, ItemClassService} from "../../shared/services/item-class.service";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ItemClassDetailsResolver implements Resolve<ItemClass> {
  constructor(private readonly itemClassService: ItemClassService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ItemClass> {
    const name = route.params['name'];
    return this.itemClassService.getItemClass(name);
  }
}
