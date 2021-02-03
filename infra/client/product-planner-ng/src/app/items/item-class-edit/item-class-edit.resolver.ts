import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {ItemClass, ItemClassService} from '../../shared/services/item-class.service';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ItemClassEditResolver implements Resolve<ItemClass> {
  constructor(private readonly itemClassService: ItemClassService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ItemClass> {
    const name = route.params.name;
    return this.itemClassService.getDraftItemClass(name).pipe(
      catchError(_ => this.itemClassService.newDraftItemClass(name))
    );
  }
}
