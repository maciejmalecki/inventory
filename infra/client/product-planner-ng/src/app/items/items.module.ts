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
import {DragDropModule} from '@angular/cdk/drag-drop';
import {SharedModule} from '../shared/shared.module';
import { ItemStockListComponent } from './item-stock-list/item-stock-list.component';
import {CdkTableModule} from '@angular/cdk/table';

@NgModule({
  declarations: [
    ItemClassDetailsComponent,
    ItemClassesComponent,
    ItemClassEditComponent,
    ItemsComponent,
    ItemDetailsComponent,
    ItemEditComponent,
    ItemStockListComponent],
  imports: [
    CommonModule,
    RouterModule,
    MatModule,
    FormsModule,
    ReactiveFormsModule,
    DragDropModule,
    SharedModule,
    CdkTableModule
  ]
})
export class ItemsModule {
}
