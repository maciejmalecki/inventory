import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemClassDetailsComponent} from './item-class-details/item-class-details.component';
import {RouterModule} from '@angular/router';
import {MatModule} from '../mat.module';
import { ItemClassesComponent } from './item-classes/item-classes.component';
import { ItemClassEditComponent } from './item-class-edit/item-class-edit.component';
import {ReactiveFormsModule} from '@angular/forms';

@NgModule({
  declarations: [ItemClassDetailsComponent, ItemClassesComponent, ItemClassEditComponent],
  imports: [
    CommonModule, RouterModule, MatModule, ReactiveFormsModule
  ]
})
export class ItemsModule {
}
