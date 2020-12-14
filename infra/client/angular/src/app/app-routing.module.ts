import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ItemClassDetailsComponent} from './items/item-class-details/item-class-details.component';
import {ItemClassResolver} from './items/item-class-details/item-class-resolver.service';
import {ItemClassesComponent} from './items/item-classes/item-classes.component';
import {ItemClassEditComponent} from './items/item-class-edit/item-class-edit.component';

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
    path: 'itemClasses/:name/edit',
    component: ItemClassEditComponent,
    resolve: {
      itemClass: ItemClassResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
