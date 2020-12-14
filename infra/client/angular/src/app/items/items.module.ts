import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemClassDetailsComponent} from './item-class-details/item-class-details.component';
import {RouterModule} from '@angular/router';
import {MatModule} from '../mat.module';
import { ItemClassesComponent } from './item-classes/item-classes.component';

@NgModule({
  declarations: [ItemClassDetailsComponent, ItemClassesComponent],
  imports: [
    CommonModule, RouterModule, MatModule
  ]
})
export class ItemsModule {
}
