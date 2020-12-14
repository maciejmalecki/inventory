import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ItemClassDetailsComponent} from './items/item-class-details/item-class-details.component';
import {ItemClassDetailsResolver} from './items/item-class-details/ItemClassDetailsResolver';
import {ItemClassesComponent} from './items/item-classes/item-classes.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: 'itemClasses',
    children: [
      {
        path: '',
        component: ItemClassesComponent
      },
      {
        path: ':name',
        component: ItemClassDetailsComponent,
        resolve: {
          itemClass: ItemClassDetailsResolver
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
