import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemClassDetailsComponent} from './item-class-details/item-class-details.component';
import {RouterModule} from '@angular/router';
import {MatModule} from '../mat.module';
import {ItemClassesComponent} from './item-classes/item-classes.component';
import {ItemClassEditComponent} from './item-class-edit/item-class-edit.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ItemsComponent} from './items/items.component';
import {ItemDetailsComponent} from './item-details/item-details.component';
import { ItemEditComponent } from './item-edit/item-edit.component';

@NgModule({
  declarations: [
    ItemClassDetailsComponent,
    ItemClassesComponent,
    ItemClassEditComponent,
    ItemsComponent,
    ItemDetailsComponent,
    ItemEditComponent],
  imports: [
    CommonModule,
    RouterModule,
    MatModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ItemsModule {
}
