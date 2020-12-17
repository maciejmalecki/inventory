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
      itemClass: ItemClassResolver
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
      item: ItemResolver
    }
  },
  {
    path: 'items/:name/edit',
    component: ItemEditComponent,
    data: {
      createMode: false
    },
    resolve: {
      item: ItemResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
