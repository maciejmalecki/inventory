import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ItemClassDetailsComponent} from './items/item-class-details/item-class-details.component';
import {ItemClassResolver} from './items/item-class-details/item-class-resolver.service';
import {ItemClassesComponent} from './items/item-classes/item-classes.component';
import {ItemClassEditComponent} from './items/item-class-edit/item-class-edit.component';
import {ItemsComponent} from './items/items/items.component';
import {ItemDetailsComponent} from './items/item-details/item-details.component';
import {ItemResolver} from './items/item.resolver';
import {ItemEditComponent} from './items/item-edit/item-edit.component';
import {CreateItemResolver} from './items/create-item.resolver';
import {ItemClassEditResolver} from './items/item-class-edit/item-class-edit.resolver';
import {AttributesResolver} from './items/attributes-resolver.service';
import {ManufacturerResolver} from './items/manufacturer.resolver';
import {ItemStockResolver} from './items/item-stock.resolver';
import {ItemStockListComponent} from './items/item-stock-list/item-stock-list.component';
import {ItemStockListResolver} from './items/item-stock-list.resolver';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: 'itemClasses',
    component: ItemClassesComponent
  },
  {
    path: 'itemClasses/:name',
    component: ItemClassDetailsComponent,
    resolve: {
      itemClass: ItemClassResolver
    }
  },
  {
    path: 'itemClasses/:name/newItem',
    component: ItemEditComponent,
    data: {
      createMode: true
    },
    resolve: {
      item: CreateItemResolver,
    }
  },
  {
    path: 'itemClasses/:name/edit',
    component: ItemClassEditComponent,
    resolve: {
      itemClass: ItemClassEditResolver,
      attributes: AttributesResolver
    }
  },
  {
    path: 'items',
    component: ItemsComponent
  },
  {
    path: 'items/:name',
    component: ItemDetailsComponent,
    resolve: {
      item: ItemResolver,
      stock: ItemStockResolver
    }
  },
  {
    path: 'items/:name/edit',
    component: ItemEditComponent,
    data: {
      createMode: false
    },
    resolve: {
      item: ItemResolver,
      manufacturers: ManufacturerResolver
    }
  },
  {
    path: 'items/:name/duplicate',
    component: ItemEditComponent,
    data: {
      createMode: true
    },
    resolve: {
      item: ItemResolver,
      manufacturers: ManufacturerResolver
    }
  },
  {
    path: 'item-stock',
    component: ItemStockListComponent,
    resolve: {
      itemStockList: ItemStockListResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
