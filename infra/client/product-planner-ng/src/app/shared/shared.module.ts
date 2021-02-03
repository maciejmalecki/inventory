import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ItemStockEditorComponent} from './components/item-stock-editor/item-stock-editor.component';
import {MatModule} from '../mat.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';



@NgModule({
  declarations: [
    ItemStockEditorComponent
  ],
  exports: [
    ItemStockEditorComponent
  ],
  imports: [
    CommonModule,
    MatModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
