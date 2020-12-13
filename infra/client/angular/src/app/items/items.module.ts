import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ItemClassDetailsComponent} from './item-class-details/item-class-details.component';
import {RouterModule} from "@angular/router";
import {MatModule} from "../mat.module";

@NgModule({
  declarations: [ItemClassDetailsComponent],
  imports: [
    CommonModule, RouterModule, MatModule
  ]
})
export class ItemsModule {
}
