import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemClassDetailsComponent} from './item-class-details/item-class-details.component';
import {RouterModule} from '@angular/router';
import {MatModule} from '../mat.module';
import {ItemClassesComponent} from './item-classes/item-classes.component';
import {ItemClassEditComponent} from './item-class-edit/item-class-edit.component';
import {ReactiveFormsModule} from '@angular/forms';
import {ItemsComponent} from './items/items.component';
import {ItemDetailsComponent} from './item-details/item-details.component';

@NgModule({
  declarations: [
    ItemClassDetailsComponent,
    ItemClassesComponent,
    ItemClassEditComponent,
    ItemsComponent,
    ItemDetailsComponent],
  imports: [
    CommonModule,
    RouterModule,
    MatModule,
    ReactiveFormsModule
  ]
})
export class ItemsModule {
}
